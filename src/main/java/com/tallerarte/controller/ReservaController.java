package com.tallerarte.controller;

import com.tallerarte.domain.Reserva;
import com.tallerarte.domain.Usuario;
import com.tallerarte.repository.UsuarioRepository;
import com.tallerarte.service.ReservaException;
import com.tallerarte.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Ruta /reserva/**: ya protegida en ConfiguracionSeguridad (solo rol CLIENTE)
@Controller
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;

    // El nombre de usuario de la sesion es el correo
    private Usuario usuarioActual(Authentication auth) {
        return usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
    }

    // Se llama desde el boton "Reservar" del detalle del taller
    @PostMapping("/crear")
    public String crear(@RequestParam Integer tallerId, Authentication auth, RedirectAttributes ra) {
        try {
            Reserva r = reservaService.reservar(usuarioActual(auth).getIdUsuario(), tallerId);
            return "redirect:/reserva/" + r.getIdReserva() + "/resumen";
        } catch (ReservaException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/taller/detalle/" + tallerId;      // vuelve al detalle del taller
        }
    }

    @GetMapping("/{id}/resumen")
    public String resumen(@PathVariable Integer id, Authentication auth, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("reserva", reservaService.obtenerDelUsuario(id, usuarioActual(auth).getIdUsuario()));
            return "reserva/resumen";
        } catch (ReservaException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/reserva/mis-reservas";
        }
    }

    @GetMapping("/mis-reservas")
    public String misReservas(Authentication auth, Model model) {
        Integer idUsuario = usuarioActual(auth).getIdUsuario();
        model.addAttribute("proximas", reservaService.listarPorUsuarioYEstado(idUsuario, Reserva.CONFIRMADA));
        model.addAttribute("asistidas", reservaService.listarPorUsuarioYEstado(idUsuario, Reserva.ASISTIDA));
        model.addAttribute("canceladas", reservaService.listarPorUsuarioYEstado(idUsuario, Reserva.CANCELADA));
        model.addAttribute("total", reservaService.listarPorUsuario(idUsuario).size());
        return "reserva/mis-reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Integer id, Authentication auth, RedirectAttributes ra) {
        try {
            reservaService.cancelarComoCliente(id, usuarioActual(auth).getIdUsuario());
            ra.addFlashAttribute("exito", "reserva.cancelada.exito");
        } catch (ReservaException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reserva/mis-reservas";
    }
}