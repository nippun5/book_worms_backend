package com.bookworms.repository;

import com.bookworms.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findAllByOrderByLikeCountDescCreatedAtDesc();

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bookId = :bookId")
    Double averageRatingForBook(@Param("bookId") Long bookId);

    @Modifying
    @Query("UPDATE Review r SET r.likeCount = r.likeCount + 1 WHERE r.id = :id")
    void incrementLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Review r SET r.likeCount = CASE WHEN r.likeCount > 0 THEN r.likeCount - 1 ELSE 0 END WHERE r.id = :id")
    void decrementLikeCount(@Param("id") Long id);

    void deleteByBookId(Long bookId);
}
