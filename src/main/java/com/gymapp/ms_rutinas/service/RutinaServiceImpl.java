package com.gymapp.ms_rutinas.service;

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

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarTodas() {
        return repository.findByActivoTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RutinaResponseDTO obtenerPorId(Long id) {
        Rutina rutina = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina de entrenamiento no encontrada o inactiva."));
        return mapearADto(rutina);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarHistorialPorMiembro(Long miembroId) {
        return repository.findByMiembroIdAndActivoTrueOrderByFechaAsignacionDesc(miembroId).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RutinaResponseDTO crear(RutinaRequestDTO dto) {
        log.info("[RUTINA] Intentando asignar nueva rutina para miembro ID: {}", dto.getMiembroId());

        // LÓGICA DE NEGOCIO ROBUSTA: Validar existencia síncrona mediante Feign
        try {
            miembroClient.obtenerPorId(dto.getMiembroId());
        } catch (FeignException.NotFound e) {
            log.error("[RUTINA] Operación rechazada: El miembro ID {} no existe en la BD.", dto.getMiembroId());
            throw new BusinessException("Validación fallida: El miembro asignado no existe.");
        }

        Rutina rutina = new Rutina(
                null,
                dto.getMiembroId(),
                dto.getEntrenadorId(),
                dto.getNombre(),
                dto.getNivel(),
                dto.getFechaAsignacion(),
                dto.getDuracionSemanas(),
                dto.getDetalleEjercicios(),
                true
        );

        Rutina guardada = repository.save(rutina);
        log.info("[RUTINA] Plan de entrenamiento guardado con éxito bajo ID: {}", guardada.getId());

        emitirEventosIntegracion(guardada);
        return mapearADto(guardada);
    }

    @Override
    @Transactional
    public RutinaResponseDTO actualizar(Long id, RutinaRequestDTO dto) {
        Rutina existente = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada."));

        if (!existente.getMiembroId().equals(dto.getMiembroId())) {
            throw new BusinessException("Integridad de datos: No se puede transferir una rutina existente a otro miembro.");
        }

        existente.setNombre(dto.getNombre());
        existente.setNivel(dto.getNivel());
        existente.setDuracionSemanas(dto.getDuracionSemanas());
        existente.setDetalleEjercicios(dto.getDetalleEjercicios());

        return mapearADto(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Rutina rutina = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada."));
        rutina.setActivo(false); // Eliminación lógica segura
        repository.save(rutina);
        log.info("[RUTINA] Rutina ID {} dada de baja lógicamente.", id);
    }

    private void emitirEventosIntegracion(Rutina rutina) {
        // Gamificación: Otorgar XP por recibir un nuevo plan
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("miembroId", rutina.getMiembroId());
            evento.put("accion", "NUEVA_RUTINA_ASIGNADA");
            evento.put("puntosBase", 20);
            gamificacionClient.enviarEvento(evento);
        } catch (Exception e) {
            log.warn("[INTEGRACION] No se pudo conectar con ms-gamificacion: {}", e.getMessage());
        }

        // Notificación: Avisar al miembro que su entrenador subió un plan
        try {
            Map<String, Object> noti = new HashMap<>();
            noti.put("miembroId", rutina.getMiembroId());
            noti.put("titulo", "¡Tu nueva rutina está lista!");
            noti.put("mensaje", "Tu entrenador ha publicado el plan: " + rutina.getNombre() + ". ¡A entrenar!");
            notificacionClient.enviarNotificacion(noti);
        } catch (Exception e) {
            log.warn("[INTEGRACION] No se pudo despachar la alerta a ms-notificaciones: {}", e.getMessage());
        }
    }

    private RutinaResponseDTO mapearADto(Rutina entity) {
        return RutinaResponseDTO.builder()
                .id(entity.getId())
                .miembroId(entity.getMiembroId())
                .entrenadorId(entity.getEntrenadorId())
                .nombre(entity.getNombre())
                .nivel(entity.getNivel())
                .fechaAsignacion(entity.getFechaAsignacion())
                .duracionSemanas(entity.getDuracionSemanas())
                .detalleEjercicios(entity.getDetalleEjercicios())
                .build();
    }
}
