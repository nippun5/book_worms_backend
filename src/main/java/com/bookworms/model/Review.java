package com.bookworms.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer rating; // 1-5

    @Column(nullable = false, length = 4000)
    private String text;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Review() {}

    public Review(Long bookId, Long userId, Integer rating, String text) {
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.text = text;
    }

    public Long getId() { return id; }

    public Long getBookId() { return bookId; }

    public Long getUserId() { return userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Instant getCreatedAt() { return createdAt; }
}
