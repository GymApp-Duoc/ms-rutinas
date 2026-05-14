package com.gymapp.ms_rutinas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutinaResponseDTO {
    private Long id;
    private String nombre;
    private String objetivo;
    private String nivel;
    private String descripcion;
}