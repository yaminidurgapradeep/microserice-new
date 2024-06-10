package com.programming;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.dto.CreatePaymentIntentRequest;
import com.programming.dto.CreatePaymentIntentResponse;
import com.programming.dto.CreatePaymentItem;
import com.programming.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreatePaymentIntentRequest paymentRequest;
    private CreatePaymentIntentResponse paymentResponse;

    @BeforeEach
    public void setup() {
        CreatePaymentItem item1 = new CreatePaymentItem();
        item1.setId("iphone4s");
        item1.setPrice(5000);

        CreatePaymentItem item2 = new CreatePaymentItem();
        item2.setId("iphone5s");
        item2.setPrice(7000);
        paymentRequest = new CreatePaymentIntentRequest();
        paymentRequest.setItems(List.of(item1, item2));

        paymentResponse = new CreatePaymentIntentResponse();
        paymentResponse.setClientSecret("test_client_secret");
    }

    @Test
    public void createPaymentIntent_ShouldReturnCreated_WhenPaymentIntentIsCreated() throws Exception {
        when(paymentService.createPaymentIntent(any(CreatePaymentIntentRequest.class)))
                .thenReturn(paymentResponse);

        mockMvc.perform(post("/api/payment/create-payment-intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated());
    }
}