package com.programming.controller;
import com.programming.dto.CreatePaymentIntentRequest;
import com.programming.dto.CreatePaymentIntentResponse;
import com.programming.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePaymentIntentResponse createPaymentIntent(@RequestBody CreatePaymentIntentRequest paymentRequest) {
        log.info("CreatePaymentIntentRequest: {}", paymentRequest);
        return paymentService.createPaymentIntent(paymentRequest);
    }
}