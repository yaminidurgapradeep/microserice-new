package com.learningmicroservices.orderservice.repository;

import com.learningmicroservices.orderservice.Enum;
import com.learningmicroservices.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByPaymentStatus(Enum.PaymentStatus paymentStatus);
}

