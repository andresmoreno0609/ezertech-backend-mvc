package com.ezertech.library.dto.request;

import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        @Size(min = 13, max = 13, message = "ISBN must have 13 characters")
        String isbn,

        @NotNull(message = "Publication year is required")
        @Min(value = 1000, message = "Publication year must be >= 1000")
        @Max(value = 2100, message = "Publication year must be valid")
        Integer publicationYear
) {
}
