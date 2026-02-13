package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.Order;
import org.tutorial.venusbackend.model.enums.ShippingMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {

    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private String shippingMethodLabel;
    private BigDecimal shippingCost;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(order.getItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList()));
        response.setCreatedAt(order.getCreatedAt());
        ShippingMethod method = order.getShippingMethod();
        if (method != null) {
            response.setShippingCost(method.getCost());
            response.setShippingMethodLabel(method.getLabel());
        }
        return response;
    }
}