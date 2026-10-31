package com.bookworms.repository;

import com.bookworms.model.Loan;
import com.bookworms.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBorrowerUserIdOrderByRequestedAtDesc(Long borrowerUserId);

    List<Loan> findByOwnerUserIdOrderByRequestedAtDesc(Long ownerUserId);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.listingId = :listingId AND l.borrowerUserId = :borrowerUserId "
         + "AND l.status IN (com.bookworms.model.LoanStatus.REQUESTED, com.bookworms.model.LoanStatus.ACTIVE)")
    boolean hasOpenRequest(@Param("listingId") Long listingId, @Param("borrowerUserId") Long borrowerUserId);

    long countByStatus(LoanStatus status);

    @Modifying
    @Query("UPDATE Loan l SET l.status = com.bookworms.model.LoanStatus.DECLINED "
         + "WHERE l.listingId = :listingId AND l.id <> :exceptLoanId AND l.status = com.bookworms.model.LoanStatus.REQUESTED")
    void declineOtherRequests(@Param("listingId") Long listingId, @Param("exceptLoanId") Long exceptLoanId);

    void deleteByBookId(Long bookId);
}
