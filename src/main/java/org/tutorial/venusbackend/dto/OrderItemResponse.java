package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.OrderItem;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long id;
    private Long bookId;
    private int quantity;
    private BigDecimal priceAtPurchase;

    public static OrderItemResponse fromEntity(OrderItem item) {
        OrderItemResponse resp = new OrderItemResponse();
        resp.setId(item.getId());
        resp.setBookId(item.getBook().getId());
        resp.setQuantity(item.getQuantity());
        resp.setPriceAtPurchase(item.getPriceAtPurchase());
        return resp;
    }
}