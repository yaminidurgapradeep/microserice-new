package com.learningmicroservices.inventoryservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void isInStock_ShouldReturnInventoryStatus() throws Exception {
		mockMvc.perform(get("/api/inventory")
						.param("skuCode", "iphone_13"))
				.andExpect(status().isOk());
	}
}
