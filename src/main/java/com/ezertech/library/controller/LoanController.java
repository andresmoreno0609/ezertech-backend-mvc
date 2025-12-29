package com.ezertech.library.controller;
import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.service.ITLoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loans", description = "Loan management operations")
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ITLoanService loanService;

    @Operation(
            summary = "Create a new loan",
            description = "Creates a loan for an available book. The loan period is 14 days."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Book not available")
    })
    @PostMapping
    public ResponseEntity<LoanResponse> create(
            @Valid @RequestBody LoanRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loanService.create(request));
    }

    @Operation(
            summary = "Return a borrowed book",
            description = "Marks the loan as returned and updates the book status to AVAILABLE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponse> returnBook(
            @PathVariable Long loanId) {

        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @Operation(
            summary = "Search loans (paginated)",
            description = "Returns a paginated list of loans with sorting options"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated loans list")
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<LoanResponse>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                loanService.search(page, size, sortBy, direction)
        );
    }
}
