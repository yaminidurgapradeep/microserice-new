package com.programming.dto;

import lombok.Data;

@Data
public class CreatePaymentIntentResponse {
    private String clientSecret;
}