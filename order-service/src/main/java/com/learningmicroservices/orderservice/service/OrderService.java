package com.learningmicroservices.orderservice.service;

import com.learningmicroservices.orderservice.Enum;
import com.learningmicroservices.orderservice.dto.InventoryResponse;
import com.learningmicroservices.orderservice.dto.OrderLineItemsDto;
import com.learningmicroservices.orderservice.dto.OrderRequest;
import com.learningmicroservices.orderservice.event.OrderPlacedEvent;
import com.learningmicroservices.orderservice.model.Order;
import com.learningmicroservices.orderservice.model.OrderLineItems;
import com.learningmicroservices.orderservice.repository.OrderRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            order.setPaymentStatus(Enum.PaymentStatus.PENDING_PAYMENT);
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successful";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

//    public void updateOrderPaymentStatus() {
//        // Find the latest order with PENDING_PAYMENT status
//        Order order = orderRepository.findFirstByPaymentStatusOrderByCreatedAtDesc(Enum.PaymentStatus.PENDING_PAYMENT);
//
//        if (order != null) {
//            // Update payment status of the order to PAYMENT_COMPLETE
//            order.setPaymentStatus(Enum.PaymentStatus.PAYMENT_COMPLETE);
//            orderRepository.save(order);
//        }
//    }

}

