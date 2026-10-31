package com.bookworms.dto;

import com.bookworms.model.ListingCondition;
import com.bookworms.model.ListingType;
import jakarta.validation.constraints.NotNull;

public record CreateListingRequest(
        @NotNull Long bookId,
        @NotNull Long ownerUserId,
        @NotNull ListingType type,
        Double price,
        ListingCondition condition,
        String note
) {}
