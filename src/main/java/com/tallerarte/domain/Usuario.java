package com.tallerarte.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 80)
    @NotBlank
    @Size(max = 80)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank
    @Email
    @Size(max = 100)
    private String correo;

    @Column(length = 20)
    @Size(max = 20)
    private String telefono;

    // Aqui se guarda la contrasena ya cifrada
    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}