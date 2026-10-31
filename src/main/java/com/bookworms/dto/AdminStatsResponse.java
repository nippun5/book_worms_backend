package com.bookworms.dto;

public record AdminStatsResponse(long users, long books, long reviews, long listings, long activeLoans) {}
