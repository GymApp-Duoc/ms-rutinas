package com.gymapp.ms_rutinas.controller;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.service.RutinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService service;

    @GetMapping
    public ResponseEntity<List<RutinaResponseDTO>> obtenerTodas() {
        log.info("[CONTROLLER] Solicitando lista de todas las rutinas activas.");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("[CONTROLLER] Buscando rutina por ID: {}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<RutinaResponseDTO>> obtenerPorMiembro(@PathVariable Long miembroId) {
        log.info("[CONTROLLER] Consultando historial de rutinas para el miembro ID: {}", miembroId);
        return ResponseEntity.ok(service.listarHistorialPorMiembro(miembroId));
    }

    @PostMapping
    public ResponseEntity<RutinaResponseDTO> crear(@Valid @RequestBody RutinaRequestDTO dto) {
        log.info("[CONTROLLER] Solicitud de nueva rutina recibida para el miembro ID: {}", dto.getMiembroId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RutinaRequestDTO dto) {
        log.info("[CONTROLLER] Solicitud para actualizar rutina ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("[CONTROLLER] Solicitud de baja lógica para la rutina ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

