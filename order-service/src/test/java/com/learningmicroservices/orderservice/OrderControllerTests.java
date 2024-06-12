package com.learningmicroservices.orderservice;


import com.learningmicroservices.orderservice.controller.OrderController;
import com.learningmicroservices.orderservice.dto.OrderRequest;
import com.learningmicroservices.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTests {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();

        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("Order Placed Successful");
        when(orderService.placeOrder(any(OrderRequest.class))).thenAnswer(invocation -> completableFuture);

        CompletableFuture<String> response = orderController.placeOrder(orderRequest);

        verify(orderService, times(1)).placeOrder(eq(orderRequest));

        response.thenAccept(result -> {
            assert result.equals("Order Placed Successful");
        });
    }

    @Test
    void testFallbackMethod() {
        OrderRequest orderRequest = new OrderRequest();
        RuntimeException runtimeException = new RuntimeException("Mock Exception");

        CompletableFuture<String> response = orderController.fallbackMethod(orderRequest, runtimeException);

        response.thenAccept(result -> {
            assert result.equals("Oops! Something went wrong, please order after some time!");
        });
    }

    @Test
    void testUpdateOrderPaymentStatus_OrderExists() {
        when(orderService.updateOrderPaymentStatus()).thenReturn(true);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        orderController.updateOrderPaymentStatus(responseMock);

        verify(orderService, times(1)).updateOrderPaymentStatus();

        verify(responseMock, times(1)).setStatus(HttpStatus.OK.value());
    }

    @Test
    void testUpdateOrderPaymentStatus_OrderNotExists() {
        when(orderService.updateOrderPaymentStatus()).thenReturn(false);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        orderController.updateOrderPaymentStatus(responseMock);

        verify(orderService, times(1)).updateOrderPaymentStatus();

        verify(responseMock, times(1)).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    }
}

