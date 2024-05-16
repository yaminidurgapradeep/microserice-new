package com.learningmicroservices.productservice;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningmicroservices.productservice.controller.ProductController;
import com.learningmicroservices.productservice.dto.ProductRequest;
import com.learningmicroservices.productservice.dto.ProductResponse;
import com.learningmicroservices.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createProductTest() throws Exception {
        ProductRequest productRequest = new ProductRequest("Product 1", "Description", BigDecimal.valueOf(100.0));
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllProductsTest() throws Exception {
        ProductResponse productResponse = new ProductResponse("1", "Product 1", "Description", BigDecimal.valueOf(100.0));
        List<ProductResponse> productResponses = Collections.singletonList(productResponse);

        given(productService.getAllProducts()).willReturn(productResponses);

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(productResponse.getId())))
                .andExpect(jsonPath("$[0].name", is(productResponse.getName())))
                .andExpect(jsonPath("$[0].description", is(productResponse.getDescription())))
                .andExpect(jsonPath("$[0].price", is(100.0)));
    }
}
