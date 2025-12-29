package com.ezertech.library.controller;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.service.ITBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(
        name = "Books",
        description = "Operations related to book management"
)
public class BookController {

    private final ITBookService bookService;

    @Operation(
            summary = "Create a new book",
            description = "Creates a new book with AVAILABLE status by default"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(request));
    }

    @Operation(
            summary = "Get book by ID",
            description = "Returns a book based on its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(
            summary = "Get all books",
            description = "Returns a list of all registered books"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Operation(
            summary = "Update a book",
            description = "Updates book information by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        return ResponseEntity.ok(bookService.update(id, request));
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes a book by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Book ID", example = "1")
            @PathVariable Long id) {

        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

