package com.ezertech.library.controller;

import com.ezertech.library.dto.request.LoanRequest;
import com.ezertech.library.dto.response.LoanResponse;
import com.ezertech.library.dto.response.PageResponse;
import com.ezertech.library.exception.LoanNotFoundException;
import com.ezertech.library.model.enums.BookStatus;
import com.ezertech.library.service.ITBookService;
import com.ezertech.library.service.ITLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoansPageController {

    private final ITLoanService loanService;
    private final ITBookService bookService;

    @GetMapping("/new")
    public String newLoan(Model model) {
        model.addAttribute("loan", new LoanRequest(null, "", "", LocalDate.now()));
        // Solo libros disponibles
        model.addAttribute("books", bookService.findByStatus(BookStatus.AVAILABLE));
        return "loans/form";
    }


    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("loan") LoanRequest request,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.findByStatus(BookStatus.AVAILABLE));
            return "loans/form";
        }
        loanService.create(request);
        redirect.addFlashAttribute("success", "Préstamo registrado exitosamente");
        return "redirect:/loans";
    }

    @GetMapping("/return/{id}")
    public String returnLoan(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            loanService.returnBook(id);
            redirect.addFlashAttribute("success", "Libro devuelto exitosamente");
        } catch (LoanNotFoundException ex) {
            redirect.addFlashAttribute("error", "No se encontró el préstamo con ID: " + id);
        }
        return "redirect:/loans";
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String query,
                       Model model) {
        PageResponse<LoanResponse> loans = loanService.search(page, size, "loanDate", "desc");
        model.addAttribute("loans", loans);
        model.addAttribute("query", query);
        return "loans/list";
    }


}



