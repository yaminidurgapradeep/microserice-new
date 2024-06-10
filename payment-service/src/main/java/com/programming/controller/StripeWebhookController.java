package com.programming.controller;

import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
//import com.programming.StripeConfig;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.stripe.net.Webhook;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class StripeWebhookController {

    @Autowired
    private final WebClient.Builder webClientBuilder;

    public StripeWebhookController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostMapping("/stripe/events")
    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
        log.info("Reached handleStripeEvent controller");

        if (sigHeader == null){
            return "";
        }

        Event event;

        String endpointSecret = "whsec_7eff260643cd420114ae903af27d34b53958646a3d524b11fbd71e31486012b8";

            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                );
            } catch (JsonSyntaxException e) {
                // Invalid payload
                return "";
            } catch (SignatureVerificationException e) {
                // Invalid signature
                return "";
            }

            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    log.info("Payment Successful");
                    webClientBuilder.build().post()
                            .uri("http://order-service/api/order/update-payment-status")
                            .retrieve()
                            .toBodilessEntity()
                            .block();
                    break;
            }

            return "";
    }
}
