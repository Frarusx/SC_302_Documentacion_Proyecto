package com.tallerarte.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioRegistro {

    // Nombre y al menos un apellido
    @Pattern(regexp = "^\\S+(\\s+\\S+)+$")
    @Size(max = 80)
    private String nombre;

    @NotBlank
    @Email
    @Size(max = 100)
    private String correo;

    // 8 digitos, se permite un espacio en medio (8888 1426)
    @Pattern(regexp = "^\\d{4}\\s?\\d{4}$")
    private String telefono;

    // Minimo 8 caracteres, con al menos una letra y un numero
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
    private String contrasena;

    @NotBlank
    private String confirmacion;
}