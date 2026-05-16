package com.gymapp.ms_rutinas.repository;

import com.gymapp.ms_rutinas.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    List<Rutina> findByActivoTrue();

    Optional<Rutina> findByIdAndActivoTrue(Long id);

    List<Rutina> findByMiembroIdAndActivoTrueOrderByFechaAsignacionDesc(Long miembroId);
}
