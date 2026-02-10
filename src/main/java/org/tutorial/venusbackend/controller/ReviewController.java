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
import org.tutorial.venusbackend.repository.ReviewRepository;
import org.tutorial.venusbackend.service.AuthHelperService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final AuthHelperService authHelperService;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(
            @RequestBody ReviewRequest request,
            @RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        boolean alreadyReviewed = reviewRepository.existsByBookAndUser(book, user);
        if (alreadyReviewed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You have already reviewed this book");
        }

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
                savedReview.getCreatedAt(),
                savedReview.getId()
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
                        r.getCreatedAt(),
                        r.getUser() != null ? r.getUser().getId() : null
                ))
                .toList();

        return ResponseEntity.ok(reviewResponses);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest request,
            @RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

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
                saved.getCreatedAt(),
                saved.getId()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own review");
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok("Review deleted successfully");
    }
}