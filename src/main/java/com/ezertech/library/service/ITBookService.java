package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.request.SearchRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;

import java.util.List;
import java.util.Map;

public interface ITBookService {

    BookResponse create(BookRequest request);

    BookResponse findById(Long id);

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);

    PageResponse<BookResponse> search(
            SearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );

    Map<String, Object> getLibraryStats();

    List<Book> findByStatus(BookStatus status);

    List<BookResponse> findAll();
}
