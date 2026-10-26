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

CREATE TABLE IF NOT EXISTS inscripciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    actividad_id INTEGER NOT NULL,
    estudiante_id INTEGER NOT NULL,
    estado TEXT NOT NULL CHECK (
        estado IN (
            'INSCRITA',
            'CANCELADA',
            'LLEGADA_AVISADA',
            'LLEGADA_VERIFICADA'
        )
    ),
    inscrito_en TEXT NOT NULL,
    cancelado_en TEXT,
    motivo_cancelacion TEXT,
    cancelada_tarde INTEGER NOT NULL DEFAULT 0
        CHECK (cancelada_tarde IN (0, 1)),
    aviso_llegada_en TEXT,
    verificado_en TEXT,
    UNIQUE (actividad_id, estudiante_id),
    FOREIGN KEY (actividad_id)
        REFERENCES actividades(id),
    FOREIGN KEY (estudiante_id)
        REFERENCES perfiles_estudiantes(usuario_id)
);

CREATE TABLE IF NOT EXISTS historial_cancelaciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    inscripcion_id INTEGER NOT NULL,
    cancelado_en TEXT NOT NULL,
    motivo TEXT,
    tardia INTEGER NOT NULL
        CHECK (tardia IN (0, 1)),
    FOREIGN KEY (inscripcion_id)
        REFERENCES inscripciones(id)
);

CREATE INDEX IF NOT EXISTS
    idx_inscripciones_actividad_estado
ON inscripciones(actividad_id, estado);

CREATE INDEX IF NOT EXISTS
    idx_historial_cancelaciones_inscripcion
ON historial_cancelaciones(inscripcion_id);
