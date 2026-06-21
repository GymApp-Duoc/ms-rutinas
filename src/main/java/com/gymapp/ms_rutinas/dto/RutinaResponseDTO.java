package com.gymapp.ms_rutinas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de respuesta de una rutina de entrenamiento")
public class RutinaResponseDTO {
    private Long id;
    private Long miembroId;
    private Long entrenadorId;
    private String nombre;
    private String nivel;
    private LocalDate fechaAsignacion;
    private Integer duracionSemanas;
    private String detalleEjercicios;
}