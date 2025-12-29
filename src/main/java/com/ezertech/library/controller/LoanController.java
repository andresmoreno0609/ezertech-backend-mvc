package com.ezertech.library.controller;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.service.ITLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ITLoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loanService.create(request));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> findAll() {
        return ResponseEntity.ok(loanService.findAll());
    }
}
