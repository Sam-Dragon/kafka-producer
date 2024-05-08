package com.kafka.product.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
