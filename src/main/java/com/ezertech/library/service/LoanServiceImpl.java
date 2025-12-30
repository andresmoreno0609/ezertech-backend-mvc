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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Optional;

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
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
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

    public PageResponse<LoanResponse> search(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by("DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Accede al query desde el contexto web (no recomendado para lógica pura, pero válido si no quieres cambiar la interfaz)
        String query = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(req -> req.getParameter("query"))
                .orElse(null);

        Page<Loan> result = (query != null && !query.trim().isEmpty())
                ? loanRepository.searchByKeyword(query.trim(), pageable)
                : loanRepository.findAll(pageable);

        return new PageResponse<>(
                result.getContent().stream().map(this::mapToResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }




    private LoanResponse mapToResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getBorrowerName(),
                loan.getBorrowerEmail(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate()
        );
    }
}
