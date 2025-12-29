package com.ezertech.library.service;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.dto.response.PageResponse;

public interface ITLoanService {

    LoanResponse create(LoanRequest request);

    LoanResponse returnBook(Long loanId);

    PageResponse<LoanResponse> search(
            int page,
            int size,
            String sortBy,
            String direction
    );
}
