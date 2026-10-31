package com.bookworms.service;

import com.bookworms.dto.AdminStatsResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserService userService;
    private final BookService bookService;
    private final ReviewService reviewService;
    private final ListingService listingService;
    private final LoanService loanService;

    public AdminService(UserService userService, BookService bookService, ReviewService reviewService,
                         ListingService listingService, LoanService loanService) {
        this.userService = userService;
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.listingService = listingService;
        this.loanService = loanService;
    }

    public AdminStatsResponse stats() {
        return new AdminStatsResponse(
                userService.countAll(),
                bookService.countAll(),
                reviewService.countAll(),
                listingService.countAll(),
                loanService.countActive()
        );
    }
}
