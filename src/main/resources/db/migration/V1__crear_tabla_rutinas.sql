
CREATE TABLE rutinas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         miembro_id BIGINT NOT NULL,
                         entrenador_id BIGINT NOT NULL,
                         nombre VARCHAR(100) NOT NULL,
                         nivel VARCHAR(50) NOT NULL,
                         fecha_asignacion DATE NOT NULL,
                         duracion_semanas INT NOT NULL,
                         detalle_ejercicios VARCHAR(1500),
                         activo BOOLEAN NOT NULL DEFAULT TRUE
);

