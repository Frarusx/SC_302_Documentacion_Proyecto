package com.tallerarte.repository;

import com.tallerarte.domain.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Evita reservar dos veces el mismo taller (si la anterior fue cancelada, si se puede)
    boolean existsByUsuarioIdUsuarioAndTallerIdTallerAndEstadoNot(Integer idUsuario, Integer idTaller, String estado);

    List<Reserva> findByUsuarioIdUsuarioOrderByFechaReservaDesc(Integer idUsuario);

    // Filtros de la vista del administrador (cualquier parametro puede venir null)
    @Query("""
           SELECT r FROM Reserva r
           WHERE (:estado IS NULL OR r.estado = :estado)
             AND (:idTaller IS NULL OR r.taller.idTaller = :idTaller)
             AND (:texto IS NULL
                  OR LOWER(r.usuario.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                  OR LOWER(r.usuario.correo) LIKE LOWER(CONCAT('%', :texto, '%')))
           ORDER BY r.fechaReserva DESC
           """)
    List<Reserva> filtrar(@Param("estado") String estado,
            @Param("idTaller") Integer idTaller,
            @Param("texto") String texto);
}