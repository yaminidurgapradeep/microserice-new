package com.programming.service;

import com.programming.dto.CreatePaymentIntentRequest;
import com.programming.dto.CreatePaymentIntentResponse;
import com.programming.dto.CreatePaymentItem;
import com.programming.model.Payment;
import com.programming.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest createPaymentIntentRequest) {
        long sum = Double.valueOf(createPaymentIntentRequest.getItems().stream().mapToDouble(CreatePaymentItem::getPrice).sum()).longValue();
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(sum * 100L)
                        .setCurrency("INR")
                        // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params);
            Payment payment = new Payment();
            payment.setAmount(paymentIntent.getAmount());
            payment.setCreated(paymentIntent.getCreated());
            payment.setClientSecret(paymentIntent.getClientSecret());
            payment.setCurrency(paymentIntent.getCurrency());
            paymentRepository.save(payment);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        CreatePaymentIntentResponse paymentResponse = new CreatePaymentIntentResponse();
        paymentResponse.setClientSecret(paymentIntent.getClientSecret());
        return paymentResponse;
    }
}