package com.ezertech.library.repository;

import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByStatus(BookStatus status);

    boolean existsByIsbn(String isbn);

    @Query("""
        SELECT b FROM Book b
        WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}