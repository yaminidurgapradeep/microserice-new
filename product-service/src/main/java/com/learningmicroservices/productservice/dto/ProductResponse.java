package com.learningmicroservices.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductResponse(String id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);

    }
}
