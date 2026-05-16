package com.gymapp.ms_rutinas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

