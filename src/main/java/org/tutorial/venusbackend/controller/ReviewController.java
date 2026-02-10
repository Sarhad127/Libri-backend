package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.dto.ReviewRequest;
import org.tutorial.venusbackend.dto.ReviewResponse;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.model.Review;
import org.tutorial.venusbackend.repository.BookRepository;
import org.tutorial.venusbackend.repository.MyUserRepository;
import org.tutorial.venusbackend.repository.ReviewRepository;
import org.tutorial.venusbackend.service.JwtService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MyUserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(
            @RequestBody ReviewRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        ReviewResponse response = new ReviewResponse(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getUser() != null && savedReview.getUser().getFirstName() != null
                        ? savedReview.getUser().getFirstName()
                        : "Anonymous",
                savedReview.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<?> getReviewsForBook(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        var reviewResponses = book.getReviews().stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getRating(),
                        r.getComment(),
                        r.getUser() != null && r.getUser().getFirstName() != null
                                ? r.getUser().getFirstName()
                                : "Anonymous",
                        r.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(reviewResponses);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only edit your own review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);

        ReviewResponse response = new ReviewResponse(
                saved.getId(),
                saved.getRating(),
                saved.getComment(),
                saved.getUser() != null && saved.getUser().getFirstName() != null
                        ? saved.getUser().getFirstName()
                        : "Anonymous",
                saved.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authHeader
    ) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own review");
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok("Review deleted successfully");
    }

    private MyUser getUserFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            return null;
        }

        return userRepository.findByEmail(email).orElse(null);
    }
}