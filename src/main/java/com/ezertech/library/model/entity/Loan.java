package com.ezertech.library.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotBlank
    @Column(name = "borrower_name", nullable = false, length = 100)
    private String borrowerName;

    @NotBlank
    @Email
    @Column(name = "borrower_email", nullable = false, length = 150)
    private String borrowerEmail;

    @NotNull
    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.loanDate != null && this.dueDate == null) {
            this.dueDate = this.loanDate.plusDays(14);
        }
    }
}
