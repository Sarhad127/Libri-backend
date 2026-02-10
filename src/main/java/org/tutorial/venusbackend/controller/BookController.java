package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutorial.venusbackend.dto.BookDTO;
import org.tutorial.venusbackend.repository.BookRepository;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

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
}