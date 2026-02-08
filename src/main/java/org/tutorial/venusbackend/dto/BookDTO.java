package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.Book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private String description;
    private String format;
    private String language;

    private String category;
    private String publisher;
    private String seriesName;
    private Integer seriesNumber;
    private String isbn;
    private int amount;
    private List<String> borrowHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
        this.amount = book.getAmount();
        this.borrowHistory = book.getBorrowHistory().stream()
                .map(bh -> bh.toString())
                .collect(Collectors.toList());
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
    }
}