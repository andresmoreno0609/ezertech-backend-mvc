package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.request.SearchRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.dto.response.PageResponse;

import java.util.List;

public interface ITBookService {

    BookResponse create(BookRequest request);

    BookResponse findById(Long id);

    List<BookResponse> findAll();

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);

    PageResponse<BookResponse> search(
            SearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
