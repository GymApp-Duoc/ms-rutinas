CREATE TABLE rutinas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL UNIQUE,
                         objetivo VARCHAR(50) NOT NULL,
                         nivel VARCHAR(50) NOT NULL,
                         descripcion TEXT NOT NULL
);

