package com.ezertech.library.controller;

import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.service.ITLoanService;
import com.ezertech.library.service.ITBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoansPageController {

    private final ITLoanService loanService;
    private final ITBookService bookService;

    // Lista de préstamos
    @GetMapping
    public String list(Model model) {
        List<LoanResponse> loans = loanService.search(0, 50, "loanDate", "DESC").content();
        model.addAttribute("loans", loans);
        return "loans/list"; // templates/loans/list.html
    }

    // Formulario para crear préstamo
    @GetMapping("/new")
    public String newLoan(@RequestParam Long bookId, Model model) {
        var book = bookService.findById(bookId);
        model.addAttribute("book", book);
        return "loans/form"; // templates/loans/form.html
    }
}

