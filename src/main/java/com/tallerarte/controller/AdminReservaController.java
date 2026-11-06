package com.tallerarte.controller;

import com.tallerarte.repository.TallerRepository;
import com.tallerarte.service.ReservaException;
import com.tallerarte.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Ruta /admin/**: ya protegida en ConfiguracionSeguridad (solo rol ADMINISTRADOR)
@Controller
@RequestMapping("/admin/reservas")
@RequiredArgsConstructor
public class AdminReservaController {

    private final ReservaService reservaService;
    private final TallerRepository tallerRepository;

    @GetMapping
    public String listar(@RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer tallerId,
            @RequestParam(required = false) String texto,
            Model model) {
        model.addAttribute("reservas", reservaService.filtrarAdmin(estado, tallerId, texto));
        model.addAttribute("talleres", tallerRepository.findAll());
        model.addAttribute("estado", estado);
        model.addAttribute("tallerId", tallerId);
        model.addAttribute("texto", texto);
        return "reserva/listado";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            reservaService.cancelarComoAdmin(id);
            ra.addFlashAttribute("exito", "reserva.cancelada.exito");
        } catch (ReservaException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservas";
    }

    @PostMapping("/{id}/asistida")
    public String asistida(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            reservaService.marcarAsistida(id);
            ra.addFlashAttribute("exito", "reserva.asistida.exito");
        } catch (ReservaException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}