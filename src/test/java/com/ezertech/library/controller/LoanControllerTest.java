package com.ezertech.library.controller;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.service.ITLoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@ExtendWith(SpringExtension.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITLoanService loanService;

    @Test
    void testCreateLoan() throws Exception {
        LoanResponse response = new LoanResponse(
                1L, 10L, "Clean Code", "Andrés", "andres@test.com",
                LocalDate.now(), LocalDate.now().plusDays(14), null
        );

        Mockito.when(loanService.create(any(LoanRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "bookId": 10,
                              "borrowerName": "Andrés",
                              "borrowerEmail": "andres@test.com",
                              "loanDate": "2025-12-29"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"))
                .andExpect(jsonPath("$.borrowerName").value("Andrés"));
    }



    @Test
    void testReturnBook() throws Exception {
        LoanResponse response = new LoanResponse(
                1L, 10L, "Clean Code", "Andrés", "andres@test.com",
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), LocalDate.now()
        );

        Mockito.when(loanService.returnBook(1L)).thenReturn(response);

        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate").exists())
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"));
    }

    @Test
    void testSearchLoans() throws Exception {
        LoanResponse loan = new LoanResponse(
                1L, 10L, "Clean Code", "Andrés", "andres@test.com",
                LocalDate.now(), LocalDate.now().plusDays(14), null
        );
        PageResponse<LoanResponse> pageResponse = new PageResponse<>(
                List.of(loan), 0, 10, 1, 1
        );

        Mockito.when(loanService.search(0, 10, "id", "ASC")).thenReturn(pageResponse);

        mockMvc.perform(get("/api/loans/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Clean Code")))
                .andExpect(content().string(containsString("Andrés")));
    }
}
