package com.learningmicroservices.orderservice;
import com.learningmicroservices.orderservice.Enum.PaymentStatus;
import com.learningmicroservices.orderservice.dto.InventoryResponse;
import com.learningmicroservices.orderservice.dto.OrderLineItemsDto;
import com.learningmicroservices.orderservice.dto.OrderRequest;
import com.learningmicroservices.orderservice.event.OrderPlacedEvent;
import com.learningmicroservices.orderservice.model.Order;
import com.learningmicroservices.orderservice.repository.OrderRepository;
import com.learningmicroservices.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceIntegrationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);

        lenient().when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);

        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        InventoryResponse[] inventoryResponses = {
                InventoryResponse.builder().skuCode("SKU123").isInStock(true).build()
        };

        lenient().when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));
    }

    @Test
    void placeOrder_AllProductsInStock_ShouldPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemsDtoList(Collections.singletonList(
                new OrderLineItemsDto("SKU123", new BigDecimal("100.00"), 1)
        ));

        InventoryResponse[] inventoryResponses = {new InventoryResponse("SKU123", true)};
        when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));

        String result = orderService.placeOrder(orderRequest);

        assertEquals("Order Placed Successful", result);
        verify(orderRepository).save(any(Order.class));
        verify(kafkaTemplate).send(eq("notificationTopic"), any(OrderPlacedEvent.class));
    }
    @Test
    void updateOrderPaymentStatus_WhenOrderExists_ShouldUpdateStatus() {
        Order existingOrder = new Order();
        existingOrder.setOrderNumber(UUID.randomUUID().toString());
        existingOrder.setPaymentStatus(PaymentStatus.PENDING_PAYMENT);
        when(orderRepository.findFirstByPaymentStatus(PaymentStatus.PENDING_PAYMENT)).thenReturn(existingOrder);

        boolean result = orderService.updateOrderPaymentStatus();

        assertTrue(result);
        assertEquals(PaymentStatus.PAYMENT_COMPLETE, existingOrder.getPaymentStatus());
        verify(orderRepository).save(existingOrder);
    }
    @Test
    void updateOrderPaymentStatus_WhenNoOrderExists_ShouldNotUpdateStatus() {
        when(orderRepository.findFirstByPaymentStatus(PaymentStatus.PENDING_PAYMENT)).thenReturn(null);

        boolean result = orderService.updateOrderPaymentStatus();

        assertFalse(result);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void placeOrder_ProductNotInStock_ShouldThrowException() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemsDtoList(Collections.singletonList(
                new OrderLineItemsDto("SKU123", new BigDecimal("100.00"), 1)
        ));

        InventoryResponse[] inventoryResponses = {new InventoryResponse("SKU123", false)};
        when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), any(OrderPlacedEvent.class));
    }
}