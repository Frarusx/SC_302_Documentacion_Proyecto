package com.tallerarte.controller;

import com.tallerarte.domain.Categoria;
import com.tallerarte.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// CRUD de categorias de talleres (pensado para el Administrador)
@Controller
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("categorias", categoriaService.getCategorias());
        return "categoria/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Categoria categoria = categoriaService.getCategoria(id);
        if (categoria == null) {
            ra.addFlashAttribute("error", "categoria.error.noexiste");
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria", categoria);
        return "categoria/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "categoria/form";
        }
        // Nombre repetido (solo al crear)
        if (categoria.getIdCategoria() == null && categoriaService.existeNombre(categoria.getNombre())) {
            result.rejectValue("nombre", "Duplicado", "Ya existe una categoría con ese nombre");
            return "categoria/form";
        }
        categoriaService.save(categoria);
        ra.addFlashAttribute("exito", "categoria.mensaje.guardada");
        return "redirect:/categoria/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            categoriaService.delete(id);
            ra.addFlashAttribute("exito", "categoria.mensaje.eliminada");
        } catch (DataIntegrityViolationException e) {
            // Tiene talleres asociados
            ra.addFlashAttribute("error", "categoria.error.contalleres");
        }
        return "redirect:/categoria/listado";
    }
}
