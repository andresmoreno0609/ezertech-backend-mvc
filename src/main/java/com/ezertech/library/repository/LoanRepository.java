package com.ezertech.library.repository;

import com.ezertech.library.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByBorrowerEmail(String borrowerEmail);
}
