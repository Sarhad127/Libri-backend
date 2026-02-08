package org.tutorial.venusbackend.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.dto.BookDTO;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.BookRepository;
import org.tutorial.venusbackend.repository.MyUserRepository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final MyUserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    @PostMapping("/{bookId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long bookId, Authentication authentication) {
        String email = authentication.getName();
        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!user.getFavoriteBooks().contains(book)) {
            user.getFavoriteBooks().add(book);
            userRepository.save(user);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Book added to favorites"));
    }

    @Transactional
    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long bookId, Authentication authentication) {
        String email = authentication.getName();
        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (user.getFavoriteBooks().contains(book)) {
            user.getFavoriteBooks().remove(book);
            userRepository.save(user);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Book removed from favorites"));
    }

    @GetMapping
    public ResponseEntity<?> getFavorites(Authentication authentication) {
        String email = authentication.getName();
        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<BookDTO> favoriteDTOs = user.getFavoriteBooks().stream()
                .map(BookDTO::new).collect(Collectors.toSet());

        return ResponseEntity.ok(favoriteDTOs);
    }
}