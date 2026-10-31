package com.bookworms.dto;

import jakarta.validation.constraints.NotNull;

public record CreateLoanRequest(@NotNull Long listingId, @NotNull Long borrowerUserId) {}
