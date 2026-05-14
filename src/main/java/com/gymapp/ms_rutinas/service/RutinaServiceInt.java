package com.gymapp.ms_rutinas.service;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import java.util.List;
import java.util.Optional;

public interface RutinaServiceInt {
    List<RutinaResponseDTO> listarTodas();
    Optional<RutinaResponseDTO> obtenerPorId(Long id);
    List<RutinaResponseDTO> buscarPorObjetivo(String objetivo);
    RutinaResponseDTO crear(RutinaRequestDTO dto);
    Optional<RutinaResponseDTO> actualizar(Long id, RutinaRequestDTO dto);
    void eliminar(Long id);
}