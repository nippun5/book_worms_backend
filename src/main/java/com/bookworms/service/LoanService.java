package com.bookworms.service;

import com.bookworms.exception.ApiException;
import com.bookworms.model.Listing;
import com.bookworms.model.ListingStatus;
import com.bookworms.model.ListingType;
import com.bookworms.model.Loan;
import com.bookworms.model.LoanStatus;
import com.bookworms.repository.ListingRepository;
import com.bookworms.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final LoanRepository loanRepository;
    private final ListingRepository listingRepository;

    public LoanService(LoanRepository loanRepository, ListingRepository listingRepository) {
        this.loanRepository = loanRepository;
        this.listingRepository = listingRepository;
    }

    public List<Loan> borrowedBy(Long userId) {
        return loanRepository.findByBorrowerUserIdOrderByRequestedAtDesc(userId);
    }

    public List<Loan> lentBy(Long userId) {
        return loanRepository.findByOwnerUserIdOrderByRequestedAtDesc(userId);
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> ApiException.notFound("Loan not found."));
    }

    @Transactional
    public Loan requestLoan(Long listingId, Long borrowerUserId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> ApiException.notFound("Listing not found."));
        if (listing.getType() != ListingType.LOAN) {
            throw ApiException.badRequest("This book is listed for sale, not for loan.");
        }
        if (listing.getOwnerUserId().equals(borrowerUserId)) {
            throw ApiException.badRequest("You can't borrow your own book.");
        }
        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw ApiException.conflict("Sorry, this book is no longer available.");
        }
        if (loanRepository.hasOpenRequest(listingId, borrowerUserId)) {
            throw ApiException.conflict("You already have an open request for this book.");
        }
        Loan loan = new Loan(listingId, listing.getBookId(), listing.getOwnerUserId(), borrowerUserId);
        return loanRepository.save(loan);
    }

    /** Owner accepts a request: the loan becomes active and the listing is marked unavailable. */
    @Transactional
    public Loan approve(Long loanId) {
        Loan loan = findById(loanId);
        Listing listing = listingRepository.findById(loan.getListingId())
                .orElseThrow(() -> ApiException.notFound("Listing not found."));
        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw ApiException.conflict("This book is already on loan to someone else.");
        }
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setDueDate(Instant.now().plus(LOAN_PERIOD_DAYS, ChronoUnit.DAYS));
        loanRepository.save(loan);

        loanRepository.declineOtherRequests(listing.getId(), loan.getId());

        listing.setStatus(ListingStatus.UNAVAILABLE);
        listingRepository.save(listing);
        return loan;
    }

    public Loan decline(Long loanId) {
        Loan loan = findById(loanId);
        loan.setStatus(LoanStatus.DECLINED);
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan markReturned(Long loanId) {
        Loan loan = findById(loanId);
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(Instant.now());
        loanRepository.save(loan);

        listingRepository.findById(loan.getListingId()).ifPresent(listing -> {
            listing.setStatus(ListingStatus.AVAILABLE);
            listingRepository.save(listing);
        });
        return loan;
    }

    public long countActive() {
        return loanRepository.countByStatus(LoanStatus.ACTIVE);
    }
}
