package com.gymapp.ms_rutinas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Objeto de transferencia para asignar o actualizar un plan de entrenamiento")
public class RutinaRequestDTO {

    @NotNull(message = "El ID del miembro es obligatorio")
    @Schema(description = "ID del miembro que recibe la rutina", example = "1")
    private Long miembroId;

    @NotNull(message = "El ID del entrenador es obligatorio")
    @Schema(description = "ID del entrenador creador del plan", example = "2")
    private Long entrenadorId;

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    @Schema(description = "Título del plan de entrenamiento", example = "Hipertrofia Tren Superior")
    private String nombre;

    @NotBlank(message = "El nivel (PRINCIPIANTE, INTERMEDIO, AVANZADO) es requerido")
    @Schema(description = "Nivel de dificultad", example = "INTERMEDIO")
    private String nivel;

    @NotNull(message = "La fecha de asignación es obligatoria")
    @Schema(description = "Fecha de inicio planificada", example = "2026-06-25")
    private LocalDate fechaAsignacion;

    @NotNull(message = "La duración en semanas es requerida")
    @Min(value = 1, message = "La rutina debe durar al menos 1 semana")
    @Max(value = 24, message = "La rutina no debe exceder las 24 semanas")
    @Schema(description = "Semanas que dura el ciclo", example = "4")
    private Integer duracionSemanas;

    @NotBlank(message = "Debe proporcionar el detalle de los ejercicios")
    @Schema(description = "Listado de ejercicios, series y repeticiones", example = "1. Press Banca 4x10, 2. Dominadas 4x8")
    private String detalleEjercicios;
}
