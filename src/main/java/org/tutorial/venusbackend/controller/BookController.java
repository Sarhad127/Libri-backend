package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.dto.BookDTO;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.repository.BookRepository;
import org.tutorial.venusbackend.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDTO> getBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO::new)
                .toList();
    }

    @GetMapping("/books/by-ids")
    public List<BookDTO> getBooksByIds(@RequestParam List<Long> ids) {
        return bookRepository.findAllById(ids)
                .stream()
                .map(BookDTO::new)
                .toList();
    }

    @GetMapping("/most-popular")
    public ResponseEntity<List<BookDTO>> getMostPopularBooks(
            @RequestParam(defaultValue = "10") int limit) {

        List<Book> books = bookService.getMostPopularBooks(limit);

        List<BookDTO> dtoList = books.stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/most-popular/recent")
    public ResponseEntity<List<BookDTO>> getMostPopularBooksRecent(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit) {

        List<Book> books = bookService.getMostPopularBooksRecent(days, limit);

        List<BookDTO> dtoList = books.stream()
                .map(BookDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<BookDTO>> getTopRatedBooks(
            @RequestParam(defaultValue = "10") int limit) {

        List<Book> books = bookService.getTopRatedBooks(limit);

        List<BookDTO> dtoList = books.stream()
                .map(BookDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}