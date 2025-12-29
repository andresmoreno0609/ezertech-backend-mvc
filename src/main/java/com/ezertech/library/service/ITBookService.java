package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;

import java.util.List;

public interface ITBookService {

    BookResponse create(BookRequest request);

    BookResponse findById(Long id);

    List<BookResponse> findAll();

    BookResponse update(Long id, BookRequest request);

    void delete(Long id);
}
