package com.horasbeca.service;

import com.horasbeca.repository.AcreditacionRepository;
import com.horasbeca.repository.PerfilRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgresoAnualTest {

    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private AcreditacionRepository acreditacionRepository;
    private ResumenHorasService resumenHorasService;

    @BeforeEach
    void preparar() {
        dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", true);
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")).execute(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("PRAGMA foreign_keys=ON");

        jdbcTemplate.update("INSERT INTO usuarios (id, correo, contrasena_hash, rol) VALUES (1, 'ana@prueba.com', 'x', 'ESTUDIANTE'), (2, 'luis@prueba.com', 'x', 'ESTUDIANTE'), (3, 'organizador@prueba.com', 'x', 'ORGANIZADOR')");
        jdbcTemplate.update("INSERT INTO perfiles_estudiantes (usuario_id, nombre, carne, ciclo_academico, porcentaje_beca) VALUES (1, 'Ana', 'A1', 3, 20), (2, 'Luis', 'L2', 2, 20)");
        jdbcTemplate.update("INSERT INTO perfiles_organizadores (usuario_id, nombre, departamento) VALUES (3, 'Daniela', 'Campus')");

        acreditacionRepository = new AcreditacionRepository(jdbcTemplate);
        resumenHorasService = new ResumenHorasService(new PerfilRepository(jdbcTemplate), new MetaAnualService(), acreditacionRepository);
    }

    @AfterEach
    void cerrar() {
        dataSource.close();
    }

    @Test
    void estudianteSinHoras() {
        ResumenHorasService.Resumen resumen = resumenHorasService.calcularParaAnio("ana@prueba.com", 2030);

        assertEquals(50, resumen.meta());
        assertEquals(0, resumen.acreditadas());
        assertEquals(50, resumen.pendientes());
        assertEquals(0, resumen.porcentaje());
        assertEquals(0, resumen.anchoBarra());
        assertTrue(acreditacionRepository.listarAnuales("ana@prueba.com", 2030).isEmpty());
        assertEquals(List.of(2030), acreditacionRepository.listarAnios("ana@prueba.com", 2030));
    }

    @Test
    void avanceParcialRespetaAnioYEstudiante() {
        guardarAcreditacion(1, 1, "2029-12-31T23:59", 5);
        guardarAcreditacion(2, 1, "2030-01-01T00:01", 10);
        guardarAcreditacion(3, 1, "2030-12-31T23:59", 10);
        guardarAcreditacion(4, 1, "2031-01-01T00:01", 2);
        guardarAcreditacion(5, 2, "2030-07-01T11:00", 7);

        ResumenHorasService.Resumen resumen = resumenHorasService.calcularParaAnio("ana@prueba.com", 2030);

        assertEquals(20, resumen.acreditadas());
        assertEquals(30, resumen.pendientes());
        assertEquals(40, resumen.porcentaje());
        assertEquals(40, resumen.anchoBarra());
        assertEquals(2, acreditacionRepository.listarAnuales("ana@prueba.com", 2030).size());
        assertEquals("Actividad 3", acreditacionRepository.listarAnuales("ana@prueba.com", 2030).get(0).actividad());
        assertEquals(5, acreditacionRepository.totalAnual("ana@prueba.com", 2029));
        assertEquals(2, acreditacionRepository.totalAnual("ana@prueba.com", 2031));
        assertEquals(7, acreditacionRepository.totalAnual("luis@prueba.com", 2030));
        assertEquals(List.of(2030, 2029), acreditacionRepository.listarAnios("ana@prueba.com", 2030));
    }

    @Test
    void metaCompleta() {
        guardarAcreditacion(1, 1, "2030-03-01T11:00", 20);
        guardarAcreditacion(2, 1, "2030-06-01T11:00", 30);

        ResumenHorasService.Resumen resumen = resumenHorasService.calcularParaAnio("ana@prueba.com", 2030);

        assertEquals(50, resumen.acreditadas());
        assertEquals(0, resumen.pendientes());
        assertEquals(100, resumen.porcentaje());
        assertEquals(100, resumen.anchoBarra());
    }

    @Test
    void excesoMuestraPorcentajeRealYLimitaLaBarra() {
        guardarAcreditacion(1, 1, "2030-03-01T11:00", 40);
        guardarAcreditacion(2, 1, "2030-06-01T11:00", 20);

        ResumenHorasService.Resumen resumen = resumenHorasService.calcularParaAnio("ana@prueba.com", 2030);

        assertEquals(60, resumen.acreditadas());
        assertEquals(0, resumen.pendientes());
        assertEquals(120, resumen.porcentaje());
        assertEquals(100, resumen.anchoBarra());
    }

    private void guardarAcreditacion(long id, long estudianteId, String acreditadoEn, int horas) {
        String fechaActividad = LocalDate.parse(acreditadoEn.substring(0, 10)).minusDays(1).toString();

        jdbcTemplate.update("INSERT INTO actividades (id, organizador_id, nombre, descripcion, ubicacion, fecha, hora_inicio, hora_fin, cupo_maximo, horas_acreditables) VALUES (?, 3, ?, 'Apoyo', 'UVG', ?, '09:00', '10:00', 7, ?)", id, "Actividad " + id, fechaActividad, horas);
        jdbcTemplate.update("INSERT INTO inscripciones (id, actividad_id, estudiante_id, estado, inscrito_en, aviso_llegada_en, verificado_en, verificado_por_id) VALUES (?, ?, ?, 'LLEGADA_VERIFICADA', ?, ?, ?, 3)", id, id, estudianteId, fechaActividad + "T08:00", fechaActividad + "T09:10", fechaActividad + "T09:15");
        jdbcTemplate.update("INSERT INTO acreditaciones (inscripcion_id, horas_acreditadas, acreditado_en) VALUES (?, ?, ?)", id, horas, acreditadoEn);
    }
}