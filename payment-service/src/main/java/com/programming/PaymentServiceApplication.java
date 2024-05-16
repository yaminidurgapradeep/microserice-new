package com.programming;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentServiceApplication {

    @PostConstruct
    public void setup(){
        Stripe.apiKey = "sk_test_51OyXRXSHahRma8MWYejurh6kIyzRdp0h5tt5wBNnLK9LxYocmttC7GsOSRhC9XCp42YWLa0o4OJqFqdPwMRpqnam00YvADml0g";
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}