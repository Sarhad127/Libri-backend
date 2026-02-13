package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.Book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private String description;
    private String format;
    private String language;
    private BigDecimal price;
    private String category;
    private String publisher;
    private String seriesName;
    private Integer seriesNumber;
    private String isbn;
    private int stock;
    private List<String> borrowHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int reviewCount;
    private Integer pages;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.imageUrl = book.getImageUrl();
        this.author = book.getAuthor() != null ? book.getAuthor().getName() : null;
        this.description = book.getDescription();
        this.format = book.getFormat();
        this.language = book.getLanguage();
        this.category = book.getCategory();
        this.publisher = book.getPublisher();
        this.seriesName = book.getSeriesName();
        this.seriesNumber = book.getSeriesNumber();
        this.isbn = book.getIsbn();
        this.stock = book.getStock();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
        this.price = book.getPrice();
        this.reviewCount = book.getReviews() != null ? book.getReviews().size() : 0;
        this.pages = book.getPages();
    }
}