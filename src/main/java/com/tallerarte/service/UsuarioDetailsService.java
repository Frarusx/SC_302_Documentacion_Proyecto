package com.tallerarte.service;

import com.tallerarte.domain.Usuario;
import com.tallerarte.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Spring Security usa esta clase para buscar al usuario en la base de datos al iniciar sesion
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));
        var roles = List.of(new SimpleGrantedAuthority(usuario.getRol().getNombre()));
        // Un usuario inactivo no puede iniciar sesion
        return new UsuarioSesion(usuario.getCorreo(), usuario.getContrasena(),
                usuario.isActivo(), roles, usuario.getNombre());
    }
}