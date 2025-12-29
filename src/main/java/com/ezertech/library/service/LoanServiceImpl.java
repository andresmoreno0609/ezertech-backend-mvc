package com.ezertech.library.service;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.exception.BookNotAvailableException;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.exception.LoanNotFoundException;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.entity.Loan;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.repository.BookRepository;
import com.ezertech.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ITLoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Override
    public LoanResponse create(LoanRequest request) {

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with id: " + request.bookId())
                );

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Book is not available for loan");
        }

        Loan loan = Loan.builder()
                .book(book)
                .borrowerName(request.borrowerName())
                .borrowerEmail(request.borrowerEmail())
                .loanDate(request.loanDate())
                .dueDate(request.loanDate().plusDays(14))
                .build();

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    public LoanResponse returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));

        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        bookRepository.save(book);

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    public List<LoanResponse> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LoanResponse mapToResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBorrowerName(),
                loan.getBorrowerEmail(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate()
        );
    }
}
