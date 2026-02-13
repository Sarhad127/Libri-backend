package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.enums.ShippingMethod;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<CartItemRequest> cartItems;
    private ShippingMethod shippingMethod;
}