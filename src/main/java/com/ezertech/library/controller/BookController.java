package com.ezertech.library.controller;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.service.ITBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            summary = "Search loans (paginated)",
            description = "Returns a paginated list of loans with sorting options"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated loans list")
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookResponse>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                bookService.search(null, page, size, sortBy, direction)
        );
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

    @Operation(
            summary = "Get library statistics",
            description = "Returns general statistics about books and loans"
    )
    @ApiResponse(responseCode = "200", description = "Library statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLibraryStats() {
        return ResponseEntity.ok(bookService.getLibraryStats());
    }
}

