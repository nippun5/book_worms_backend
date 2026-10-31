package com.bookworms.service;

import com.bookworms.exception.ApiException;
import com.bookworms.model.Review;
import com.bookworms.model.ReviewLike;
import com.bookworms.repository.ReviewLikeRepository;
import com.bookworms.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewLikeRepository reviewLikeRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
    }

    public List<Review> forBook(Long bookId) {
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
    }

    public List<Review> byUser(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Review> recent() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Review> trending() {
        return reviewRepository.findAllByOrderByLikeCountDescCreatedAtDesc();
    }

    public List<Review> all() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public double averageRating(Long bookId) {
        Double avg = reviewRepository.averageRatingForBook(bookId);
        return avg == null ? 0.0 : avg;
    }

    public Set<Long> likedReviewIds(Long userId) {
        return reviewLikeRepository.findByUserId(userId).stream()
                .map(ReviewLike::getReviewId)
                .collect(Collectors.toSet());
    }

    public Review add(Long bookId, Long userId, int rating, String text) {
        Review review = new Review(bookId, userId, rating, text.trim());
        return reviewRepository.save(review);
    }

    @Transactional
    public boolean toggleLike(Long reviewId, Long userId) {
        reviewRepository.findById(reviewId).orElseThrow(() -> ApiException.notFound("Review not found."));
        var existing = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId);
        if (existing.isPresent()) {
            reviewLikeRepository.delete(existing.get());
            reviewRepository.decrementLikeCount(reviewId);
            return false;
        } else {
            reviewLikeRepository.save(new ReviewLike(reviewId, userId));
            reviewRepository.incrementLikeCount(reviewId);
            return true;
        }
    }

    @Transactional
    public void delete(Long reviewId) {
        reviewLikeRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    public long countAll() {
        return reviewRepository.count();
    }
}
