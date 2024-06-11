package com.learningmicroservices.orderservice.dto;

import com.learningmicroservices.orderservice.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Enum.PaymentStatus paymentStatus;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
