package com.programming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePaymentIntentRequest {
    private List<CreatePaymentItem> items;

    public CreatePaymentIntentRequest() {

    }
}