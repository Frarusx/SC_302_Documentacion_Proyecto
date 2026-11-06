package com.tallerarte.controller;

import com.tallerarte.domain.Categoria;
import com.tallerarte.domain.Taller;
import com.tallerarte.domain.Usuario;
import com.tallerarte.service.CategoriaService;
import com.tallerarte.service.TallerService;
import com.tallerarte.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/taller")
@RequiredArgsConstructor
public class TallerController {

    private final TallerService tallerService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    // ---------- Catalogo publico (lo ve el Cliente) ----------

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) Integer categoria, Model model) {
        model.addAttribute("talleres", tallerService.getCatalogo(categoria));
        model.addAttribute("categorias", categoriaService.getCategorias());
        model.addAttribute("categoriaSeleccionada", categoria);
        return "taller/catalogo";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Taller taller = tallerService.getTaller(id);
        if (taller == null || !taller.isActivo()) {
            ra.addFlashAttribute("error", "taller.error.noexiste");
            return "redirect:/taller/catalogo";
        }
        model.addAttribute("taller", taller);
        return "taller/detalle";
    }

    // ---------- Gestion del Instructor ----------

    @GetMapping("/mis-talleres")
    public String misTalleres(Model model, RedirectAttributes ra) {
        Usuario instructor = instructorActual();
        if (instructor == null) {
            ra.addFlashAttribute("error", "taller.error.sininstructor");
            return "redirect:/";
        }
        model.addAttribute("talleres", tallerService.getTalleresDeInstructor(instructor.getIdUsuario()));
        return "taller/mis-talleres";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Taller taller = new Taller();
        model.addAttribute("taller", taller);
        model.addAttribute("categorias", categoriaService.getCategorias());
        return "taller/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Taller taller = tallerService.getTaller(id);
        if (!esDelInstructor(taller)) {
            ra.addFlashAttribute("error", "taller.error.noexiste");
            return "redirect:/taller/mis-talleres";
        }
        model.addAttribute("taller", taller);
        model.addAttribute("categorias", categoriaService.getCategorias());
        return "taller/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("taller") Taller taller, BindingResult result,
                          @RequestParam(required = false) Integer idCategoria,
                          Model model, RedirectAttributes ra) {

        // La categoria llega como id desde el select
        Categoria categoria = (idCategoria == null) ? null : categoriaService.getCategoria(idCategoria);
        if (categoria == null) {
            result.rejectValue("categoria", "NotNull", "Seleccione una categoría");
        }
        taller.setCategoria(categoria);

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.getCategorias());
            return "taller/form";
        }

        if (taller.getIdTaller() != null) {
            // Edicion: solo el instructor dueno puede cambiarlo
            Taller existente = tallerService.getTaller(taller.getIdTaller());
            if (!esDelInstructor(existente)) {
                ra.addFlashAttribute("error", "taller.error.noexiste");
                return "redirect:/taller/mis-talleres";
            }
            taller.setInstructor(existente.getInstructor());
        } else {
            Usuario instructor = instructorActual();
            if (instructor == null) {
                ra.addFlashAttribute("error", "taller.error.sininstructor");
                return "redirect:/";
            }
            taller.setInstructor(instructor);
        }

        tallerService.save(taller);
        ra.addFlashAttribute("exito", "taller.mensaje.guardado");
        return "redirect:/taller/mis-talleres";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        Taller taller = tallerService.getTaller(id);
        if (!esDelInstructor(taller)) {
            ra.addFlashAttribute("error", "taller.error.noexiste");
            return "redirect:/taller/mis-talleres";
        }
        try {
            tallerService.delete(id);
            ra.addFlashAttribute("exito", "taller.mensaje.eliminado");
        } catch (DataIntegrityViolationException e) {
            // Tiene reservas asociadas: no se puede borrar, se puede desactivar
            ra.addFlashAttribute("error", "taller.error.conreservas");
        }
        return "redirect:/taller/mis-talleres";
    }

    // ---------- Apoyo ----------

    // Devuelve el instructor que inicio sesion (el nombre de usuario es el correo)
    private Usuario instructorActual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()) {
            return null;
        }
        return usuarioService.getUsuarioPorCorreo(autenticacion.getName());
    }
    

    // Verifica que el taller exista y pertenezca al instructor actual
    private boolean esDelInstructor(Taller taller) {
        Usuario instructor = instructorActual();
        return taller != null && instructor != null
                && taller.getInstructor().getIdUsuario().equals(instructor.getIdUsuario());
    }
}
