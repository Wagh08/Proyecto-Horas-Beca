package com.horasbeca.repository;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AcreditacionRepositoryTest {

    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private AcreditacionRepository acreditacionRepository;

    @BeforeEach
    void preparar() {
        dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", true);
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")).execute(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("PRAGMA foreign_keys=ON");

        jdbcTemplate.update("INSERT INTO usuarios (id, correo, contrasena_hash, rol) VALUES (1, 'estudiante@prueba.com', 'x', 'ESTUDIANTE'), (2, 'organizador@prueba.com', 'x', 'ORGANIZADOR')");
        jdbcTemplate.update("INSERT INTO perfiles_estudiantes (usuario_id, nombre, carne, ciclo_academico, porcentaje_beca) VALUES (1, 'Andrea', '12345', 3, 20)");
        jdbcTemplate.update("INSERT INTO perfiles_organizadores (usuario_id, nombre, departamento) VALUES (2, 'Daniela', 'Biblioteca')");
        jdbcTemplate.update("INSERT INTO actividades (id, organizador_id, nombre, descripcion, ubicacion, fecha, hora_inicio, hora_fin, cupo_maximo, horas_acreditables) VALUES (1, 2, 'Apoyo', 'Descripción', 'Biblioteca', '2030-01-01', '09:00', '10:00', 7, 4), (2, 2, 'Otra actividad', 'Descripción', 'Campus', '2030-01-01', '09:00', '10:00', 7, 2)");
        jdbcTemplate.update("INSERT INTO inscripciones (id, actividad_id, estudiante_id, estado, inscrito_en, aviso_llegada_en, verificado_en) VALUES (1, 1, 1, 'LLEGADA_VERIFICADA', '2030-01-01T08:00', '2030-01-01T09:00', '2030-01-01T09:05')");
        jdbcTemplate.update("INSERT INTO codigos_qr (inscripcion_id, token, creado_en) VALUES (1, '123e4567-e89b-42d3-a456-426614174000', '2030-01-01T10:00')");

        acreditacionRepository = new AcreditacionRepository(jdbcTemplate);
    }

    @AfterEach
    void cerrar() {
        dataSource.close();
    }

    @Test
    void guardaHorasConfiguradasUnaSolaVez() {
        assertEquals(1, registrar(1, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 1));
        assertEquals(0, registrar(1, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 2));
        assertEquals(1, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM acreditaciones", Integer.class));
        assertEquals(4, jdbcTemplate.queryForObject("SELECT horas_acreditadas FROM acreditaciones WHERE inscripcion_id = 1", Integer.class));
        assertEquals(4, acreditacionRepository.totalAnual("estudiante@prueba.com", 2030));
        assertEquals(0, acreditacionRepository.totalAnual("estudiante@prueba.com", 2029));
    }

    @Test
    void rechazaAntesDeFinalizarYConOtraActividad() {
        assertEquals(0, registrar(1, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 9, 30));
        assertEquals(0, registrar(2, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 1));
        assertEquals(0, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM acreditaciones", Integer.class));
    }

    @Test
    void rechazaOrganizadorAjenoYCodigoInventado() {
        assertEquals(0, registrar(1, "otro@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 1));
        assertEquals(0, registrar(1, "organizador@prueba.com", "00000000-0000-4000-8000-000000000000", 10, 1));
        assertEquals(0, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM acreditaciones", Integer.class));
    }

    @Test
    void rechazaLlegadaSinVerificarYCancelacion() {
        jdbcTemplate.update("UPDATE inscripciones SET estado = 'LLEGADA_AVISADA', verificado_en = NULL WHERE id = 1");
        assertEquals(0, registrar(1, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 1));

        jdbcTemplate.update("UPDATE inscripciones SET estado = 'CANCELADA' WHERE id = 1");
        assertEquals(0, registrar(1, "organizador@prueba.com", "123e4567-e89b-42d3-a456-426614174000", 10, 1));
        assertEquals(0, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM acreditaciones", Integer.class));
    }

    private int registrar(long actividadId, String organizador, String token, int hora, int minuto) {
        return acreditacionRepository.registrarSiValida(actividadId, organizador, token, LocalDateTime.of(2030, 1, 1, hora, minuto));
    }
}