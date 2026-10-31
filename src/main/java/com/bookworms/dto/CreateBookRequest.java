package com.bookworms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequest(
        @NotBlank String title,
        @NotBlank String author,
        String genre,
        String description,
        @NotNull Long addedByUserId
) {}
