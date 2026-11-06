package com.tallerarte.service;

import com.tallerarte.domain.Reserva;
import com.tallerarte.domain.Taller;
import com.tallerarte.domain.Usuario;
import com.tallerarte.repository.ReservaRepository;
import com.tallerarte.repository.TallerRepository;
import com.tallerarte.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final TallerRepository tallerRepository;
    private final UsuarioRepository usuarioRepository;

    /** Reservar un cupo: descuenta de Taller.cupos. */
    @Transactional
    public Reserva reservar(Integer idUsuario, Integer idTaller) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ReservaException("reserva.error.usuario"));
        // Se bloquea la fila del taller hasta terminar la transaccion (evita sobreventa de cupos)
        Taller taller = tallerRepository.findByIdForUpdate(idTaller)
                .orElseThrow(() -> new ReservaException("reserva.error.tallerNoExiste"));

        if (!taller.isActivo()) {
            throw new ReservaException("reserva.error.tallerInactivo");
        }
        if (taller.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new ReservaException("reserva.error.tallerPasado");
        }
        if (taller.getCupos() <= 0) {
            throw new ReservaException("reserva.error.sinCupos");
        }
        if (reservaRepository.existsByUsuarioIdUsuarioAndTallerIdTallerAndEstadoNot(
                idUsuario, idTaller, Reserva.CANCELADA)) {
            throw new ReservaException("reserva.error.duplicada");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setTaller(taller);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado(Reserva.CONFIRMADA);

        taller.setCupos(taller.getCupos() - 1);
        tallerRepository.save(taller);
        return reservaRepository.save(reserva);
    }

    /** El cliente cancela SU reserva. */
    @Transactional
    public void cancelarComoCliente(Integer idReserva, Integer idUsuario) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaException("reserva.error.noExiste"));
        if (!reserva.getUsuario().getIdUsuario().equals(idUsuario)) {   // privacidad: solo sus reservas
            throw new ReservaException("reserva.error.noExiste");
        }
        cancelar(reserva);
    }

    /** Vista admin: cancelar cualquier reserva. */
    @Transactional
    public void cancelarComoAdmin(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaException("reserva.error.noExiste"));
        cancelar(reserva);
    }

    /** Vista admin: marcar una reserva confirmada como asistida. */
    @Transactional
    public void marcarAsistida(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaException("reserva.error.noExiste"));
        if (!Reserva.CONFIRMADA.equals(reserva.getEstado())) {
            throw new ReservaException("reserva.error.noModificable");
        }
        reserva.setEstado(Reserva.ASISTIDA);
        reservaRepository.save(reserva);
    }

    private void cancelar(Reserva reserva) {
        if (!Reserva.CONFIRMADA.equals(reserva.getEstado())) {      // ya asistida o cancelada
            throw new ReservaException("reserva.error.noCancelable");
        }
        Taller taller = tallerRepository.findByIdForUpdate(reserva.getTaller().getIdTaller())
                .orElseThrow(() -> new ReservaException("reserva.error.tallerNoExiste"));

        reserva.setEstado(Reserva.CANCELADA);
        taller.setCupos(taller.getCupos() + 1);                      // devuelve el cupo
        tallerRepository.save(taller);
        reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public Reserva obtenerDelUsuario(Integer idReserva, Integer idUsuario) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaException("reserva.error.noExiste"));
        if (!reserva.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new ReservaException("reserva.error.noExiste");
        }
        return reserva;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(Integer idUsuario) {
        return reservaRepository.findByUsuarioIdUsuarioOrderByFechaReservaDesc(idUsuario);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuarioYEstado(Integer idUsuario, String estado) {
        return listarPorUsuario(idUsuario).stream()
                .filter(r -> r.getEstado().equals(estado)).toList();
    }

    @Transactional(readOnly = true)
    public List<Reserva> filtrarAdmin(String estado, Integer idTaller, String texto) {
        String e = (estado == null || estado.isBlank()) ? null : estado;
        String t = (texto == null || texto.isBlank()) ? null : texto.trim();
        return reservaRepository.filtrar(e, idTaller, t);
    }
}