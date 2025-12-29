package com.ezertech.library.dto.response;

import com.ezertech.library.model.enums.BookStatus;

import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        BookStatus status,
        LocalDateTime createdAt
) {}
