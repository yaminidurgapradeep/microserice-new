package com.programming.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreatePaymentIntentRequest {
    private List<CreatePaymentItem> items;
}