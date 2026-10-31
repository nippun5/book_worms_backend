package com.bookworms.controller;

import com.bookworms.dto.CreateListingRequest;
import com.bookworms.dto.UpdateListingStatusRequest;
import com.bookworms.model.Listing;
import com.bookworms.service.ListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    /** Browse the marketplace. Optionally scope to one book or one owner. */
    @GetMapping
    public List<Listing> getAll(@RequestParam(required = false) Long bookId,
                                 @RequestParam(required = false) Long ownerUserId) {
        if (bookId != null) return listingService.byBook(bookId);
        if (ownerUserId != null) return listingService.byOwner(ownerUserId);
        return listingService.all();
    }

    @GetMapping("/{id}")
    public Listing getOne(@PathVariable Long id) {
        return listingService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Listing> create(@Valid @RequestBody CreateListingRequest req) {
        Listing listing = listingService.create(
                req.bookId(), req.ownerUserId(), req.type(), req.price(), req.condition(), req.note());
        return ResponseEntity.status(HttpStatus.CREATED).body(listing);
    }

    @PatchMapping("/{id}/status")
    public Listing updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateListingStatusRequest req) {
        return listingService.updateStatus(id, req.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        listingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
