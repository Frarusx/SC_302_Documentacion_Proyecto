package com.tallerarte.service;

import com.tallerarte.domain.Categoria;
import com.tallerarte.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria getCategoria(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        return categoriaRepository.existsByNombreIgnoreCase(nombre);
    }

    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
