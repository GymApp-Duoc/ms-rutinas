package com.gymapp.ms_rutinas.service;

import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.exception.BusinessException;
import com.gymapp.ms_rutinas.exception.RecursoNoEncontradoException;
import com.gymapp.ms_rutinas.model.Rutina;
import com.gymapp.ms_rutinas.repository.RutinaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RutinaService implements RutinaServiceInt {

    private final RutinaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> listarTodas() {
        log.info("Consultando todas las rutinas");
        return repository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RutinaResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapearADto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponseDTO> buscarPorObjetivo(String objetivo) {
        return repository.findByObjetivoIgnoreCase(objetivo).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RutinaResponseDTO crear(RutinaRequestDTO dto) {
        if (repository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BusinessException("Ya existe una rutina con el nombre: " + dto.getNombre());
        }
        Rutina rutina = new Rutina(null, dto.getNombre(), dto.getObjetivo(), dto.getNivel(), dto.getDescripcion());
        return mapearADto(repository.save(rutina));
    }

    @Override
    @Transactional
    public Optional<RutinaResponseDTO> actualizar(Long id, RutinaRequestDTO dto) {
        return repository.findById(id).map(existente -> {
            if (!existente.getNombre().equalsIgnoreCase(dto.getNombre()) &&
                    repository.existsByNombreIgnoreCase(dto.getNombre())) {
                throw new BusinessException("Ya existe otra rutina con el nombre: " + dto.getNombre());
            }
            existente.setNombre(dto.getNombre());
            existente.setObjetivo(dto.getObjetivo());
            existente.setNivel(dto.getNivel());
            existente.setDescripcion(dto.getDescripcion());
            return mapearADto(repository.save(existente));
        });
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Rutina con ID " + id + " no encontrada.");
        }
        repository.deleteById(id);
        log.info("Rutina eliminada: {}", id);
    }

    private RutinaResponseDTO mapearADto(Rutina rutina) {
        return new RutinaResponseDTO(rutina.getId(), rutina.getNombre(), rutina.getObjetivo(), rutina.getNivel(), rutina.getDescripcion());
    }
}
