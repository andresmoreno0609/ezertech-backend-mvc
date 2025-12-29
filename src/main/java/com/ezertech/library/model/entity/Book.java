package com.ezertech.library.model.entity;

import com.ezertech.library.model.enums.BookStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String author;

    @NotBlank
    @Size(min = 13, max = 13)
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Min(1000)
    @Max(2100)
    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = BookStatus.AVAILABLE;
        }
    }
}
