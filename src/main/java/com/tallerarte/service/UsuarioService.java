package com.tallerarte.service;

import com.tallerarte.domain.FormularioRegistro;
import com.tallerarte.domain.Usuario;
import com.tallerarte.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final PasswordEncoder cifradoContrasena;

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo.trim().toLowerCase());
    }

    @Transactional
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    // Registro desde la pagina publica: siempre queda como Cliente y con la contrasena cifrada
    @Transactional
    public void registrarCliente(FormularioRegistro formulario) {
        Usuario usuario = new Usuario();
        usuario.setNombre(formulario.getNombre().trim());
        usuario.setCorreo(formulario.getCorreo().trim().toLowerCase());
        usuario.setTelefono(formulario.getTelefono().replaceAll("\\s", ""));
        usuario.setContrasena(cifradoContrasena.encode(formulario.getContrasena()));
        usuario.setActivo(true);
        usuario.setRol(rolService.getRolPorNombre("ROLE_CLIENTE"));
        usuarioRepository.save(usuario);
    }
}