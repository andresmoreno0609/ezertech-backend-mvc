package com.ezertech.library.controller;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.service.ITBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ExtendWith(SpringExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITBookService bookService;

    @Test
    void testCreateBook() throws Exception {
        BookResponse response = new BookResponse(
                1L, "Clean Code", "Robert C. Martin", "9780132350884",
                2008, BookStatus.AVAILABLE, LocalDateTime.now()
        );

        Mockito.when(bookService.create(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Clean Code",
                                  "author": "Robert C. Martin",
                                  "isbn": "9780132350884",
                                  "publicationYear": 2008,
                                  "status": "AVAILABLE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));
    }

    @Test
    void testFindById() throws Exception {
        BookResponse response = new BookResponse(
                1L, "Refactoring", "Martin Fowler", "9780201485677",
                1999, BookStatus.AVAILABLE, LocalDateTime.now()
        );

        Mockito.when(bookService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"))
                .andExpect(jsonPath("$.author").value("Martin Fowler"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookResponse response = new BookResponse(
                1L, "Domain-Driven Design", "Eric Evans", "9780321125217",
                2003, BookStatus.AVAILABLE, LocalDateTime.now()
        );

        Mockito.when(bookService.update(eq(1L), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Domain-Driven Design",
                                  "author": "Eric Evans",
                                  "isbn": "9780321125217",
                                  "publicationYear": 2003,
                                  "status": "AVAILABLE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Domain-Driven Design"))
                .andExpect(jsonPath("$.author").value("Eric Evans"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService, Mockito.times(1)).delete(1L);
    }

    @Test
    void testGetLibraryStats() throws Exception {
        Mockito.when(bookService.getLibraryStats()).thenReturn(Map.of(
                "totalBooks", 10,
                "availableBooks", 7,
                "borrowedBooks", 3
        ));

        mockMvc.perform(get("/api/books/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("totalBooks")))
                .andExpect(content().string(containsString("availableBooks")))
                .andExpect(content().string(containsString("borrowedBooks")));
    }
}
