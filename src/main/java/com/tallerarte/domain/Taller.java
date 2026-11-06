package com.tallerarte.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Entity
@Table(name = "taller")
public class Taller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taller")
    private Integer idTaller;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @Column(length = 500)
    @Size(max = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal precio;

    // Cupos totales del taller. El modulo de reservas descuenta de aqui.
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer cupos;

    @Column(name = "fecha_hora", nullable = false)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHora;

    @Column(length = 100)
    @Size(max = 100)
    private String lugar;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Instructor dueno del taller (usuario con rol instructor)
    @ManyToOne
    @JoinColumn(name = "id_instructor", nullable = false)
    private Usuario instructor;
}
