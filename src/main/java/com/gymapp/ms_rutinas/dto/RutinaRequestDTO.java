package com.gymapp.ms_rutinas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutinaRequestDTO {

    @NotNull(message = "El ID del miembro es obligatorio")
    private Long miembroId;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long entrenadorId;

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El nivel (PRINCIPIANTE, INTERMEDIO, AVANZADO) es requerido")
    private String nivel;

    @NotNull(message = "La fecha de asignación es obligatoria")
    private LocalDate fechaAsignacion;

    @NotNull(message = "La duración en semanas es requerida")
    @Min(value = 1, message = "La rutina debe durar al menos 1 semana")
    @Max(value = 24, message = "La rutina no debe exceder las 24 semanas")
    private Integer duracionSemanas;

    @NotBlank(message = "Debe proporcionar el detalle de los ejercicios")
    private String detalleEjercicios;
}

