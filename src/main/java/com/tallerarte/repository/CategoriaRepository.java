package com.tallerarte.repository;

import com.tallerarte.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);
}
