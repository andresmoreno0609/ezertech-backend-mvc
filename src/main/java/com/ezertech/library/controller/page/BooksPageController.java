package com.ezertech.library.controller.page;

import com.ezertech.library.dto.request.BookRequest;
import com.ezertech.library.dto.response.BookResponse;
import com.ezertech.library.exception.BookDeletionException;
import com.ezertech.library.exception.BookNotFoundException;
import com.ezertech.library.exception.DuplicateIsbnException;
import com.ezertech.library.service.ITBookService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksPageController {

    private final ITBookService bookService;

    // Lista de libros
    @GetMapping
    public String list(Model model) {
        // Aquí puedes usar tu servicio para traer todos los libros
        List<BookResponse> books = bookService.search(null, 0, 50, "id", "ASC").content();
        model.addAttribute("books", books);
        return "books/list"; // templates/books/list.html
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        BookResponse book = bookService.findById(id); // devuelve DTO con id
        BookRequest form = new BookRequest(
                book.id(),
                book.title(),
                book.author(),
                book.isbn(),
                book.publicationYear(),
                book.status()
        );
        model.addAttribute("book", form);
        model.addAttribute("statuses", List.of("AVAILABLE", "BORROWED", "RESERVED"));
        return "books/form";
    }

    // Búsqueda
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        var result = bookService.search(new com.ezertech.library.dto.request.SearchRequest(query),
                0, 50, "title", "ASC");
        model.addAttribute("query", query);
        model.addAttribute("books", result.content());
        return "books/search";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new BookRequest(null,"", "", "", null, null));
        model.addAttribute("statuses", List.of("AVAILABLE", "BORROWED", "RESERVED"));
        return "books/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("book") BookRequest request,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", List.of("AVAILABLE", "BORROWED", "RESERVED"));
            return "books/form";
        }

        try {
            if (request.id() != null) {
                bookService.update(request.id(), request); // ✅ debe entrar aquí
                redirect.addFlashAttribute("success", "Libro actualizado exitosamente");
            } else {
                bookService.create(request); // ❌ si el id no llega, entra aquí
                redirect.addFlashAttribute("success", "Libro creado exitosamente");
            }
            return "redirect:/books";
        } catch (DuplicateIsbnException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("statuses", List.of("AVAILABLE", "BORROWED", "RESERVED"));
            return "books/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookService.delete(id);
            redirect.addFlashAttribute("success", "Libro eliminado exitosamente");
        } catch (BookNotFoundException ex) {
            redirect.addFlashAttribute("error", "No se encontró el libro con ID: " + id);
        } catch (BookDeletionException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/books";
    }

    @GetMapping("/export")
    public void exportBooksToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=libros.csv");

        List<BookResponse> books = bookService.findAll();

        PrintWriter writer = response.getWriter();
        writer.println("Título,Autor,ISBN,Estado");

        for (BookResponse book : books) {
            writer.printf("%s,%s,%s,%s%n",
                    book.title(),
                    book.author(),
                    book.isbn(),
                    book.status());
        }
    }

}

