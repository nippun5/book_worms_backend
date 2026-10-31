package com.bookworms.controller;

import com.bookworms.dto.CreateReviewRequest;
import com.bookworms.model.Review;
import com.bookworms.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /** Dashboard feed. tab = trending (default) | recent */
    @GetMapping("/reviews")
    public List<Review> feed(@RequestParam(defaultValue = "trending") String tab) {
        return "recent".equalsIgnoreCase(tab) ? reviewService.recent() : reviewService.trending();
    }

    @GetMapping("/books/{bookId}/reviews")
    public List<Review> forBook(@PathVariable Long bookId) {
        return reviewService.forBook(bookId);
    }

    @GetMapping("/books/{bookId}/rating")
    public Map<String, Object> averageRating(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.forBook(bookId);
        return Map.of("averageRating", reviewService.averageRating(bookId), "reviewCount", reviews.size());
    }

    @GetMapping("/users/{userId}/reviews")
    public List<Review> byUser(@PathVariable Long userId) {
        return reviewService.byUser(userId);
    }

    @GetMapping("/users/{userId}/liked-review-ids")
    public Set<Long> likedReviewIds(@PathVariable Long userId) {
        return reviewService.likedReviewIds(userId);
    }

    @PostMapping("/reviews")
    public ResponseEntity<Review> add(@Valid @RequestBody CreateReviewRequest req) {
        Review review = reviewService.add(req.bookId(), req.userId(), req.rating(), req.text());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    /** Toggles the current user's like on a review. Returns {"liked": true|false}. */
    @PostMapping("/reviews/{id}/like")
    public Map<String, Boolean> toggleLike(@PathVariable Long id, @RequestParam Long userId) {
        return Map.of("liked", reviewService.toggleLike(id, userId));
    }

    /** Admin: remove a review. */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
