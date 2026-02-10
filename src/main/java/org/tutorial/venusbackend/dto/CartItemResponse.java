package org.tutorial.venusbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.CartItem;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private String imageUrl;
    private String authorName;
    private String format;
    private String language;
    private BigDecimal price;

    public static CartItemResponse fromEntity(CartItem item) {
        Book book = item.getBook();
        return new CartItemResponse(
                item.getId(),
                book.getId(),
                book.getTitle(),
                item.getQuantity(),
                book.getImageUrl(),
                book.getAuthor() != null ? book.getAuthor().getName() : "Unknown",
                book.getFormat(),
                book.getLanguage(),
                book.getPrice()
        );
    }
}