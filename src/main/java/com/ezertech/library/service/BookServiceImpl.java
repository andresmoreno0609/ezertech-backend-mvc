package com.ezertech.library.service;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.model.entity.Book;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements ITBookService {

    private final BookRepository bookRepository;

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
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublicationYear(request.publicationYear());

        return mapToResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
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
