package com.ezertech.library.repository;

import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByStatus(BookStatus status);

    boolean existsByIsbn(String isbn);
}