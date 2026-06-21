package com.gymapp.ms_rutinas.controller;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Rutinas", description = "Operaciones de gestión y asignación de planes de entrenamiento")
public class RutinaController {

    private final RutinaService service;

    @Operation(summary = "Obtener todas las rutinas activas")
    @GetMapping
    public ResponseEntity<List<RutinaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar rutina por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RutinaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Obtener el historial de rutinas de un miembro específico")
    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<RutinaResponseDTO>> obtenerPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(service.listarHistorialPorMiembro(miembroId));
    }

    @Operation(summary = "Asignar una nueva rutina")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rutina asignada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o miembro inexistente")
    })
    @PostMapping
    public ResponseEntity<RutinaResponseDTO> crear(@Valid @RequestBody RutinaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar datos de una rutina")
    @PutMapping("/{id}")
    public ResponseEntity<RutinaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RutinaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Dar de baja una rutina (Borrado lógico)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Reporte 1: Total de rutinas activas asignadas")
    @GetMapping("/reportes/total")
    public ResponseEntity<Long> contarRutinasActivas() {
        return ResponseEntity.ok(service.contarRutinasActivas());
    }

    @Operation(summary = "Reporte 2: Listar rutinas por nivel (Ej: PRINCIPIANTE, AVANZADO)")
    @GetMapping("/reportes/nivel/{nivel}")
    public ResponseEntity<List<RutinaResponseDTO>> obtenerPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(service.listarRutinasPorNivel(nivel));
    }

    @Operation(summary = "Reporte 3: Listar rutinas de larga duración (filtro por semanas)")
    @GetMapping("/reportes/duracion-larga")
    public ResponseEntity<List<RutinaResponseDTO>> obtenerRutinasLargas(@RequestParam(defaultValue = "8") Integer semanas) {
        return ResponseEntity.ok(service.listarRutinasLargas(semanas));
    }

    @Operation(summary = "Reporte 4: Rutinas asignadas en los últimos X días")
    @GetMapping("/reportes/recientes")
    public ResponseEntity<List<RutinaResponseDTO>> obtenerRecientes(@RequestParam(defaultValue = "15") int dias) {
        return ResponseEntity.ok(service.listarRutinasRecientes(dias));
    }

    @Operation(summary = "Reporte 5: Conteo de rutinas activas por entrenador")
    @GetMapping("/reportes/entrenador/{entrenadorId}/total")
    public ResponseEntity<Long> contarPorEntrenador(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(service.contarRutinasPorEntrenador(entrenadorId));
    }
}