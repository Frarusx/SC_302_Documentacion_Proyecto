package com.tallerarte.service;

import com.tallerarte.domain.Rol;
import com.tallerarte.repository.RolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    // Se usa, por ejemplo, para asignar ROLE_CLIENTE al registrarse
    @Transactional(readOnly = true)
    public Rol getRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre).orElse(null);
    }
}