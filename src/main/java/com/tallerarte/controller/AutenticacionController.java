package com.tallerarte.controller;

import com.tallerarte.domain.FormularioRegistro;
import com.tallerarte.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AutenticacionController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("formularioRegistro", new FormularioRegistro());
        return "usuario/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("formularioRegistro") FormularioRegistro formulario,
            BindingResult resultado, RedirectAttributes aviso) {

        if (!Objects.equals(formulario.getContrasena(), formulario.getConfirmacion())) {
            resultado.rejectValue("confirmacion", "registro.confirmacion.distinta");
        }
        if (!resultado.hasFieldErrors("correo") && usuarioService.existeCorreo(formulario.getCorreo())) {
            resultado.rejectValue("correo", "registro.correo.repetido");
        }
        if (resultado.hasErrors()) {
            return "usuario/registro"; // vuelve al formulario sin perder lo escrito
        }
        usuarioService.registrarCliente(formulario);
        aviso.addFlashAttribute("registrado", true);
        return "redirect:/login";
    }
}