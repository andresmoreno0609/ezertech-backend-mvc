package com.ezertech.library.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record LoanRequest(

        @NotNull(message = "Book ID is required")
        Long bookId,

        @NotBlank(message = "Borrower name is required")
        String borrowerName,

        @NotBlank(message = "Borrower email is required")
        @Email(message = "Email must be valid")
        String borrowerEmail,

        @NotNull(message = "Loan date is required")
        LocalDate loanDate
) {}
