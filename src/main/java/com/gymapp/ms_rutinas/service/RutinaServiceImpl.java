package com.gymapp.ms_rutinas.service;

import com.gymapp.ms_rutinas.assembler.RutinaAssembler;
import com.gymapp.ms_rutinas.client.GamificacionClient;
import com.gymapp.ms_rutinas.client.MiembroClient;
import com.gymapp.ms_rutinas.client.NotificacionClient;
import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.exception.BusinessException;
import com.gymapp.ms_rutinas.exception.RecursoNoEncontradoException;
import com.gymapp.ms_rutinas.model.Rutina;
import com.gymapp.ms_rutinas.repository.RutinaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RutinaServiceImpl implements RutinaService {

    private final RutinaRepository repository;
    private final MiembroClient miembroClient;
    private final GamificacionClient gamificacionClient;
    private final NotificacionClient notificacionClient;
    private final RutinaAssembler assembler; // Inyectado

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarTodas() {
        return repository.findByActivoTrue().stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RutinaResponseDTO obtenerPorId(Long id) {
        Rutina rutina = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada o inactiva."));
        return assembler.toResponseDTO(rutina);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarHistorialPorMiembro(Long miembroId) {
        return repository.findByMiembroIdAndActivoTrueOrderByFechaAsignacionDesc(miembroId).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RutinaResponseDTO crear(RutinaRequestDTO dto) {
        log.info("[RUTINA] Intentando asignar rutina para miembro ID: {}", dto.getMiembroId());

        try {
            miembroClient.obtenerPorId(dto.getMiembroId());
        } catch (FeignException.NotFound e) {
            log.error("[RUTINA] Miembro ID {} no existe.", dto.getMiembroId());
            throw new BusinessException("Validación fallida: El miembro asignado no existe.");
        }

        Rutina rutina = assembler.toEntity(dto);
        Rutina guardada = repository.save(rutina);
        log.info("[RUTINA] Plan guardado bajo ID: {}", guardada.getId());

        emitirEventosIntegracion(guardada);
        return assembler.toResponseDTO(guardada);
    }

    @Override
    @Transactional
    public RutinaResponseDTO actualizar(Long id, RutinaRequestDTO dto) {
        Rutina existente = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada."));

        if (!existente.getMiembroId().equals(dto.getMiembroId())) {
            throw new BusinessException("No se puede transferir una rutina existente a otro miembro.");
        }

        existente.setNombre(dto.getNombre());
        existente.setNivel(dto.getNivel());
        existente.setDuracionSemanas(dto.getDuracionSemanas());
        existente.setDetalleEjercicios(dto.getDetalleEjercicios());

        return assembler.toResponseDTO(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Rutina rutina = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada."));
        rutina.setActivo(false);
        repository.save(rutina);
        log.info("[RUTINA] Rutina ID {} dada de baja lógicamente.", id);
    }

    // ==========================================
    // REPORTES DE NEGOCIO
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public long contarRutinasActivas() {
        return repository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarRutinasPorNivel(String nivel) {
        return repository.findByNivelIgnoreCaseAndActivoTrue(nivel).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarRutinasLargas(Integer semanasMinimas) {
        return repository.findByDuracionSemanasGreaterThanEqualAndActivoTrue(semanasMinimas).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarRutinasRecientes(int dias) {
        LocalDate fechaCorte = LocalDate.now().minusDays(dias);
        return repository.findByFechaAsignacionAfterAndActivoTrue(fechaCorte).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarRutinasPorEntrenador(Long entrenadorId) {
        return repository.countByEntrenadorIdAndActivoTrue(entrenadorId);
    }

    private void emitirEventosIntegracion(Rutina rutina) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("miembroId", rutina.getMiembroId());
            evento.put("accion", "NUEVA_RUTINA_ASIGNADA");
            evento.put("puntosBase", 20);
            gamificacionClient.enviarEvento(evento);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Fallo ms-gamificacion: {}", e.getMessage());
        }

        try {
            Map<String, Object> noti = new HashMap<>();
            noti.put("miembroId", rutina.getMiembroId());
            noti.put("titulo", "¡Tu nueva rutina está lista!");
            noti.put("mensaje", "Tu entrenador ha publicado el plan: " + rutina.getNombre());
            notificacionClient.enviarNotificacion(noti);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Fallo ms-notificaciones: {}", e.getMessage());
        }
    }
}