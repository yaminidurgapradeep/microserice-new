package com.learningmicroservices.orderservice;


import com.learningmicroservices.orderservice.dto.OrderRequest;
import com.learningmicroservices.orderservice.Enum.PaymentStatus;
import com.learningmicroservices.orderservice.dto.OrderLineItemsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void placeOrder_ShouldReturnCreatedStatus() throws Exception {
		OrderLineItemsDto lineItem = new OrderLineItemsDto(null, "iphone_13", new BigDecimal("999.99"), 1);
		OrderRequest orderRequest = new OrderRequest(PaymentStatus.PENDING_PAYMENT, Collections.singletonList(lineItem));

		String orderRequestJson = "{\"orderLineItemsDtoList\":[{\"skuCode\":\"iphone_13\",\"price\":999.99,\"quantity\":1}]}";

		mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(orderRequestJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void updateOrderPaymentStatus_ShouldUpdateStatus() throws Exception {
		mockMvc.perform(post("/api/order/update-payment-status"))
				.andExpect(status().isBadGateway());
	}

	@Test
	public void updateOrderPaymentStatus_WhenNoPendingPaymentOrder_ShouldReturnNotAcceptable() throws Exception {
		mockMvc.perform(post("/api/order/update-payment-status"))
				.andExpect(status().isNotAcceptable());
	}
}
