package com.bookworms.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "review_likes")
@IdClass(ReviewLike.Key.class)
public class ReviewLike {

    @Id
    private Long reviewId;

    @Id
    private Long userId;

    public ReviewLike() {}

    public ReviewLike(Long reviewId, Long userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public Long getReviewId() { return reviewId; }
    public Long getUserId() { return userId; }

    public static class Key implements Serializable {
        private Long reviewId;
        private Long userId;

        public Key() {}
        public Key(Long reviewId, Long userId) {
            this.reviewId = reviewId;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key key)) return false;
            return Objects.equals(reviewId, key.reviewId) && Objects.equals(userId, key.userId);
        }

        @Override
        public int hashCode() { return Objects.hash(reviewId, userId); }
    }
}
