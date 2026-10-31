package com.bookworms.repository;

import com.bookworms.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByBookId(Long bookId);
    List<Listing> findByOwnerUserId(Long ownerUserId);
    void deleteByBookId(Long bookId);
}
