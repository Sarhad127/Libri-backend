package org.tutorial.venusbackend.dto;

import lombok.Data;
import org.tutorial.venusbackend.model.Book;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String authorName;
    private String imageUrl;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.imageUrl = book.getImageUrl();
        this.authorName = book.getAuthor() != null ? book.getAuthor().getName() : null;
    }
}