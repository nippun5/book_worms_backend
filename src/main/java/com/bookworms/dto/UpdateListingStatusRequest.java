package com.bookworms.dto;

import com.bookworms.model.ListingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateListingStatusRequest(@NotNull ListingStatus status) {}
