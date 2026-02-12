package org.tutorial.venusbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<CartItemRequest> cartItems;
    private ShippingMethodRequest shippingMethod;
}