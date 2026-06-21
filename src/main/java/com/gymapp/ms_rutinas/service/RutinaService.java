package com.gymapp.ms_rutinas.service;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import java.time.LocalDate;
import java.util.List;


public interface RutinaService {
    List<RutinaResponseDTO> listarTodas();
    RutinaResponseDTO obtenerPorId(Long id);
    List<RutinaResponseDTO> listarHistorialPorMiembro(Long miembroId);
    RutinaResponseDTO crear(RutinaRequestDTO dto);
    RutinaResponseDTO actualizar(Long id, RutinaRequestDTO dto);
    void eliminar(Long id);


    long contarRutinasActivas();
    List<RutinaResponseDTO> listarRutinasPorNivel(String nivel);
    List<RutinaResponseDTO> listarRutinasLargas(Integer semanasMinimas);
    List<RutinaResponseDTO> listarRutinasRecientes(int dias);
    long contarRutinasPorEntrenador(Long entrenadorId);
}