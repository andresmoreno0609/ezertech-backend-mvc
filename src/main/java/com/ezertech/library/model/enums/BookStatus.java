package com.ezertech.library.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Book availability status")
public enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED
}
