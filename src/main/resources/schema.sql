CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    correo TEXT NOT NULL UNIQUE,
    contrasena_hash TEXT NOT NULL,
    rol TEXT NOT NULL CHECK (rol IN ('ESTUDIANTE', 'ORGANIZADOR'))
);

CREATE TABLE IF NOT EXISTS perfiles_estudiantes (
    usuario_id INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    carne TEXT NOT NULL UNIQUE,
    ciclo_academico INTEGER NOT NULL CHECK (ciclo_academico >= 1),
    porcentaje_beca INTEGER NOT NULL CHECK (porcentaje_beca BETWEEN 1 AND 100),
    telefono TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS perfiles_organizadores (
    usuario_id INTEGER PRIMARY KEY,
    nombre TEXT NOT NULL,
    departamento TEXT NOT NULL,
    telefono TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
CREATE TABLE IF NOT EXISTS actividades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    organizador_id INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    requisitos TEXT,
    ubicacion TEXT NOT NULL,
    fecha TEXT NOT NULL,
    hora_inicio TEXT NOT NULL,
    hora_fin TEXT NOT NULL,
    cupo_maximo INTEGER NOT NULL CHECK (cupo_maximo > 0),
    horas_acreditables INTEGER NOT NULL CHECK (horas_acreditables > 0),
    FOREIGN KEY (organizador_id) REFERENCES perfiles_organizadores(usuario_id),
    CHECK (hora_fin > hora_inicio)
);