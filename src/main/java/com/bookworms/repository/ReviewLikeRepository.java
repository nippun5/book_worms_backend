package com.bookworms.repository;

import com.bookworms.model.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLike.Key> {
    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);
    List<ReviewLike> findByUserId(Long userId);
    void deleteByReviewId(Long reviewId);
}
