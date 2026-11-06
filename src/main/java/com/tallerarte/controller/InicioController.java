package com.tallerarte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    // Pagina de inicio publica
    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}