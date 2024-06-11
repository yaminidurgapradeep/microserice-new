package com.programming;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentServiceApplication {

    @PostConstruct
    public void setup(){
        Stripe.apiKey = "sk_test_51PH1GmSBC2UiAEfDhIBuAfMxKcNWNJ94epEVvjPCpmXgP6YGK1pJYfBvnMlGuDrLtbsKIAsFWtijdpZ0YwWih80L00ySMXcnSZ";
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}