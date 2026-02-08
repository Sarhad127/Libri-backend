package org.tutorial.venusbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.repository.BookRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookRepository.findAll().stream()
                .map(BookDto::new)
                .toList();
    }

    static class BookDto {
        public Long id;
        public String title;
        public String category;
        public String format;
        public String language;
        public String seriesName;
        public Integer seriesNumber;
        public String publisher;
        public String description;
        public String author;
        public int amount;
        public String isbn;
        public LocalDateTime createdAt;
        public LocalDateTime updatedAt;
        public String imageUrl;
        public BigDecimal price;

        public BookDto(Book book) {
            this.id = book.getId();
            this.title = book.getTitle();
            this.category = book.getCategory();
            this.format = book.getFormat();
            this.language = book.getLanguage();
            this.seriesName = book.getSeriesName();
            this.seriesNumber = book.getSeriesNumber();
            this.publisher = book.getPublisher();
            this.description = book.getDescription();
            this.author = book.getAuthor().getName();
            this.amount = book.getStock();
            this.isbn = book.getIsbn();
            this.createdAt = book.getCreatedAt();
            this.updatedAt = book.getUpdatedAt();
            this.imageUrl = book.getImageUrl();
            this.price = book.getPrice();
        }
    }
}