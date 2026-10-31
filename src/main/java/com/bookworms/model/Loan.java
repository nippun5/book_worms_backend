package com.bookworms.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long listingId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long ownerUserId; // lender

    @Column(nullable = false)
    private Long borrowerUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.REQUESTED;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt = Instant.now();

    private Instant dueDate;

    private Instant returnedAt;

    public Loan() {}

    public Loan(Long listingId, Long bookId, Long ownerUserId, Long borrowerUserId) {
        this.listingId = listingId;
        this.bookId = bookId;
        this.ownerUserId = ownerUserId;
        this.borrowerUserId = borrowerUserId;
    }

    public Long getId() { return id; }

    public Long getListingId() { return listingId; }

    public Long getBookId() { return bookId; }

    public Long getOwnerUserId() { return ownerUserId; }

    public Long getBorrowerUserId() { return borrowerUserId; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public Instant getRequestedAt() { return requestedAt; }

    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }

    public Instant getReturnedAt() { return returnedAt; }
    public void setReturnedAt(Instant returnedAt) { this.returnedAt = returnedAt; }
}
