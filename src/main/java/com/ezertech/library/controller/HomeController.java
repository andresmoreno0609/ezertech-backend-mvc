package com.ezertech.library.controller;

import com.ezertech.library.service.ITBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ITBookService bookService;

    @GetMapping("/")
    public String index(Model model) {
        // Obtener estadísticas desde el servicio
        Map<String, Object> stats = bookService.getLibraryStats();

        // Pasarlas al modelo para Thymeleaf
        model.addAttribute("stats", stats);

        // Renderizar la vista index.html
        return "index";
    }
}
