package com.gymapp.ms_rutinas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "rutinas")
@NoArgsConstructor
@AllArgsConstructor
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "miembro_id", nullable = false)
    private Long miembroId;

    @Column(name = "entrenador_id", nullable = false)
    private Long entrenadorId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String nivel; // Ejemplo: PRINCIPIANTE, INTERMEDIO, AVANZADO

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "duracion_semanas", nullable = false)
    private Integer duracionSemanas;

    @Column(name = "detalle_ejercicios", length = 1500)
    private String detalleEjercicios;

    @Column(nullable = false)
    private boolean activo;
}
