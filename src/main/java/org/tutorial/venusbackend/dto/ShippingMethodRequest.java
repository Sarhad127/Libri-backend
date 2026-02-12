package org.tutorial.venusbackend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingMethodRequest {
    private String label;
    private BigDecimal price;
}