package com.ezertech.library.repository;

import com.ezertech.library.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByBorrowerEmail(String borrowerEmail);

    long countByReturnDateIsNull();

    @Query("""
        SELECT COUNT(l)
        FROM Loan l
        WHERE l.returnDate IS NULL
        AND l.dueDate < :today
    """)
    long countOverdueLoans(@Param("today") LocalDate today);

    boolean existsByBookId(Long bookId);
}
