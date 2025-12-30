package com.ezertech.library.service;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.exception.BookNotAvailableException;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.exception.LoanNotFoundException;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.entity.Loan;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.repository.BookRepository;
import com.ezertech.library.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void testCreateLoanSuccess() {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin", "9780132350884",
                2008, BookStatus.AVAILABLE, null);
        LoanRequest request = new LoanRequest(book.getId(), "Andrés", "andres@test.com",LocalDate.now());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> {
            Loan l = inv.getArgument(0);
            l.setId(10L);
            return l;
        });

        LoanResponse response = loanService.create(request);

        assertNotNull(response);
        assertEquals("Clean Code", response.bookTitle());
        assertEquals(BookStatus.BORROWED, book.getStatus());
        verify(bookRepository, times(1)).save(book);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoanThrowsBookNotFound() {
        LoanRequest request = new LoanRequest(99L, "Andrés", "andres@test.com",LocalDate.now());
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> loanService.create(request));
    }

    @Test
    void testCreateLoanThrowsBookNotAvailable() {
        Book book = new Book(1L, "Refactoring", "Martin Fowler", "9780201485677",
                1999, BookStatus.BORROWED, null);
        LoanRequest request = new LoanRequest(book.getId(), "Andrés", "andres@test.com",LocalDate.now());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, () -> loanService.create(request));
    }

    @Test
    void testReturnBookSuccess() {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin", "9780132350884",
                2008, BookStatus.BORROWED, null);
        Loan loan = Loan.builder()
                .id(10L)
                .book(book)
                .borrowerName("Andrés")
                .borrowerEmail("andres@test.com")
                .loanDate(LocalDate.now().minusDays(5))
                .dueDate(LocalDate.now().plusDays(9))
                .build();

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));
        when(bookRepository.save(book)).thenReturn(book);
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanResponse response = loanService.returnBook(10L);

        assertNotNull(response.returnDate());
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
        verify(bookRepository, times(1)).save(book);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testReturnBookThrowsLoanNotFound() {
        when(loanRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> loanService.returnBook(42L));
    }

    @Test
    void testSearchReturnsPage() {
        Loan loan = Loan.builder()
                .id(10L)
                .book(new Book(1L, "Clean Code", "Robert C. Martin", "9780132350884",
                        2008, BookStatus.AVAILABLE, null))
                .borrowerName("Andrés")
                .borrowerEmail("andres@test.com")
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        Page<Loan> page = new PageImpl<>(List.of(loan));
        when(loanRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageResponse<LoanResponse> response = loanService.search(0, 10, "loanDate", "ASC");

        assertEquals(1, response.totalElements());
        assertEquals("Clean Code", response.content().get(0).bookTitle());
    }
}
