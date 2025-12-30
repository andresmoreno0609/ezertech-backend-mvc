package com.ezertech.library.dto.response;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        Long bookId,
        String bookTitle,
        String borrowerName,
        String borrowerEmail,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate
) {}
