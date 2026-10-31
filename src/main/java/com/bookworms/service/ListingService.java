package com.bookworms.service;

import com.bookworms.exception.ApiException;
import com.bookworms.model.Listing;
import com.bookworms.model.ListingCondition;
import com.bookworms.model.ListingStatus;
import com.bookworms.model.ListingType;
import com.bookworms.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> all() {
        return listingRepository.findAll();
    }

    public List<Listing> byBook(Long bookId) {
        return listingRepository.findByBookId(bookId);
    }

    public List<Listing> byOwner(Long ownerUserId) {
        return listingRepository.findByOwnerUserId(ownerUserId);
    }

    public Listing findById(Long id) {
        return listingRepository.findById(id).orElseThrow(() -> ApiException.notFound("Listing not found."));
    }

    public Listing create(Long bookId, Long ownerUserId, ListingType type, Double price,
                           ListingCondition condition, String note) {
        if (type == ListingType.SALE && (price == null || price <= 0)) {
            throw ApiException.badRequest("Enter a price greater than 0 for a book you want to sell.");
        }
        Listing listing = new Listing(
                bookId, ownerUserId, type,
                type == ListingType.SALE ? price : null,
                condition == null ? ListingCondition.GOOD : condition,
                note == null ? "" : note.trim()
        );
        return listingRepository.save(listing);
    }

    public Listing updateStatus(Long id, ListingStatus status) {
        Listing listing = findById(id);
        listing.setStatus(status);
        return listingRepository.save(listing);
    }

    public void delete(Long id) {
        findById(id); // 404 if missing
        listingRepository.deleteById(id);
    }

    public long countAll() {
        return listingRepository.count();
    }
}
