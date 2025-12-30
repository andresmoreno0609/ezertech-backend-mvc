package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.exception.BookDeletionException;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.exception.DuplicateIsbnException;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.repository.BookRepository;
import com.ezertech.library.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testCreateBook() {
        BookRequest request = new BookRequest(null,"Clean Code","Robert C. Martin","9780132350884",2008,BookStatus.AVAILABLE);
        Book book = new Book(1L, request.title(), request.author(), request.isbn(), request.publicationYear(), request.status(), LocalDateTime.now());

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.create(request);

        assertNotNull(response);
        assertEquals("Clean Code", response.title());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testFindByIdReturnsBook() {
        Book book = new Book(1L,"Refactoring","Martin Fowler","9780201485677",1999,BookStatus.AVAILABLE,LocalDateTime.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.findById(1L);

        assertEquals("Refactoring", response.title());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdThrowsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findById(99L));
    }

    @Test
    void testUpdateThrowsDuplicateIsbn() {
        Book existing = new Book(2L,"Other","Author","9780321125217",2003,BookStatus.AVAILABLE,LocalDateTime.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.findByIsbn("9780321125217")).thenReturn(Optional.of(existing));

        BookRequest request = new BookRequest(1L,"Domain-Driven Design","Eric Evans","9780321125217",2003,BookStatus.AVAILABLE);

        assertThrows(DuplicateIsbnException.class, () -> bookService.update(1L, request));
    }

    @Test
    void testDeleteBookRemovesEntityWhenNoLoans() {
        Long id = 1L;
        Book book = new Book(id,"Refactoring","Martin Fowler","9780201485677",1999,BookStatus.AVAILABLE,LocalDateTime.now());

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(loanRepository.existsByBookId(id)).thenReturn(false);

        bookService.delete(id);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBookFailsWhenLoansExist() {
        Long id = 1L;
        Book book = new Book(id,"Refactoring","Martin Fowler","9780201485677",1999,BookStatus.AVAILABLE,LocalDateTime.now());

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(loanRepository.existsByBookId(id)).thenReturn(true);

        assertThrows(BookDeletionException.class, () -> bookService.delete(id));
        verify(bookRepository, never()).delete(book);
    }

    @Test
    void testDeleteBookThrowsNotFound() {
        when(bookRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.delete(42L));
    }
}
