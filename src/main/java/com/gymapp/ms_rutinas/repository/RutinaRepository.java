package com.gymapp.ms_rutinas.repository;

import com.gymapp.ms_rutinas.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    List<Rutina> findByObjetivoIgnoreCase(String objetivo);
}