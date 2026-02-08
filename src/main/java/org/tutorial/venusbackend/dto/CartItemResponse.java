package org.tutorial.venusbackend.dto;

import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.CartItem;

import java.math.BigDecimal;

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

    public CartItemResponse(Long id, Long bookId, String bookTitle, int quantity, String imageUrl,
                            String authorName, String format, String language, BigDecimal price) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.authorName = authorName;
        this.format = format;
        this.language = language;
        this.price = price;
    }

    public Long getId() { return id; }
    public Long getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getAuthorName() { return authorName; }
    public String getFormat() { return format; }
    public String getLanguage() { return language; }
    public BigDecimal getPrice() { return price; }

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