package com.tallerarte.repository;

import com.tallerarte.domain.Taller;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TallerRepository extends JpaRepository<Taller, Integer> {

    // Catalogo del cliente: solo talleres activos, por fecha
    List<Taller> findByActivoTrueOrderByFechaHoraAsc();

    List<Taller> findByActivoTrueAndCategoriaIdCategoriaOrderByFechaHoraAsc(Integer idCategoria);

    // Talleres de un instructor
    List<Taller> findByInstructorIdUsuarioOrderByFechaHoraAsc(Integer idUsuario);

    // Modulo de reservas: bloquea la fila del taller mientras se descuenta o devuelve un cupo
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Taller t WHERE t.idTaller = :id")
    Optional<Taller> findByIdForUpdate(@Param("id") Integer id);
}