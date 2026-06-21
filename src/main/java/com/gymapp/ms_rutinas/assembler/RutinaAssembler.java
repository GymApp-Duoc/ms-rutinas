package com.gymapp.ms_rutinas.assembler;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.model.Rutina;
import org.springframework.stereotype.Component;

@Component
public class RutinaAssembler {

    public RutinaResponseDTO toResponseDTO(Rutina rutina) {
        if (rutina == null) return null;
        return RutinaResponseDTO.builder()
                .id(rutina.getId())
                .miembroId(rutina.getMiembroId())
                .entrenadorId(rutina.getEntrenadorId())
                .nombre(rutina.getNombre())
                .nivel(rutina.getNivel())
                .fechaAsignacion(rutina.getFechaAsignacion())
                .duracionSemanas(rutina.getDuracionSemanas())
                .detalleEjercicios(rutina.getDetalleEjercicios())
                .build();
    }

    public Rutina toEntity(RutinaRequestDTO dto) {
        if (dto == null) return null;
        Rutina rutina = new Rutina();
        rutina.setMiembroId(dto.getMiembroId());
        rutina.setEntrenadorId(dto.getEntrenadorId());
        rutina.setNombre(dto.getNombre());
        rutina.setNivel(dto.getNivel());
        rutina.setFechaAsignacion(dto.getFechaAsignacion());
        rutina.setDuracionSemanas(dto.getDuracionSemanas());
        rutina.setDetalleEjercicios(dto.getDetalleEjercicios());
        rutina.setActivo(true);
        return rutina;
    }
}