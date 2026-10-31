package com.bookworms.controller;

import com.bookworms.dto.CreateLoanRequest;
import com.bookworms.model.Loan;
import com.bookworms.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /** Books the given user has borrowed. */
    @GetMapping("/borrowed/{userId}")
    public List<Loan> borrowed(@PathVariable Long userId) {
        return loanService.borrowedBy(userId);
    }

    /** Books the given user has lent out (including pending requests). */
    @GetMapping("/lent/{userId}")
    public List<Loan> lent(@PathVariable Long userId) {
        return loanService.lentBy(userId);
    }

    @PostMapping
    public ResponseEntity<Loan> request(@Valid @RequestBody CreateLoanRequest req) {
        Loan loan = loanService.requestLoan(req.listingId(), req.borrowerUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    /** Lender accepts the request; the listing becomes unavailable and competing requests auto-decline. */
    @PatchMapping("/{id}/approve")
    public Loan approve(@PathVariable Long id) {
        return loanService.approve(id);
    }

    @PatchMapping("/{id}/decline")
    public Loan decline(@PathVariable Long id) {
        return loanService.decline(id);
    }

    /** Marks the book returned and makes the listing available again. */
    @PatchMapping("/{id}/return")
    public Loan markReturned(@PathVariable Long id) {
        return loanService.markReturned(id);
    }
}
