package com.gymapp.ms_rutinas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutinaRequestDTO {
    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String nombre;

    @NotBlank(message = "El objetivo (ej. Fuerza, Pérdida de peso) es obligatorio")
    private String objetivo;

    @NotBlank(message = "El nivel (ej. Principiante, Avanzado) es obligatorio")
    private String nivel;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}

