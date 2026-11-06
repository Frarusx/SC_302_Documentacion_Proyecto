package com.tallerarte.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

// Usuario de la sesion: ademas del correo guarda el nombre para mostrarlo en el menu
public class UsuarioSesion extends User {

    private final String nombreCompleto;

    public UsuarioSesion(String correo, String contrasena, boolean activo,
            Collection<? extends GrantedAuthority> roles, String nombreCompleto) {
        super(correo, contrasena, activo, true, true, true, roles);
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
}