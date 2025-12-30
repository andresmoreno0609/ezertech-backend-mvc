package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.request.SearchRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.exception.BookDeletionException;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.exception.DuplicateIsbnException;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.repository.BookRepository;
import com.ezertech.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements ITBookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Override
    public BookResponse create(BookRequest request) {

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .publicationYear(request.publicationYear())
                .status(BookStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        return mapToResponse(book);
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id)); // Validar ISBN duplicado
        Optional<Book> existing = bookRepository.findByIsbn(request.isbn());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new DuplicateIsbnException("El ISBN ya existe: " + request.isbn());
        }
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublicationYear(request.publicationYear());
        book.setStatus(request.status());
        return mapToResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        // Verificar si el libro existe
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        // Validar si tiene préstamos asociados
        if (loanRepository.existsByBookId(id)) {
            throw new BookDeletionException("No se puede eliminar el libro porque tiene préstamos activos.");
        }

        bookRepository.delete(book);
    }


    @Override
    public PageResponse<BookResponse> search(
            SearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortBy
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> result;

        if (request != null && request.keyword() != null && !request.keyword().isBlank()) {
            result = bookRepository.searchByKeyword(request.keyword(), pageable);
        } else {
            result = bookRepository.findAll(pageable);
        }

        return new PageResponse<>(
                result.getContent().stream().map(this::mapToResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public Map<String, Object> getLibraryStats() {

        long totalBooks = bookRepository.count();

        long availableBooks = bookRepository.countByStatus(BookStatus.AVAILABLE);
        long borrowedBooks = bookRepository.countByStatus(BookStatus.BORROWED);

        long activeLoans = loanRepository.countByReturnDateIsNull();

        long overdueLoans = loanRepository.countOverdueLoans(LocalDate.now());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("borrowedBooks", borrowedBooks);
        stats.put("activeLoans", activeLoans);
        stats.put("overdueLoans", overdueLoans);

        return stats;
    }

    @Override
    public List<Book> findByStatus(BookStatus status) {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getStatus(),
                book.getCreatedAt()
        );
    }
}
