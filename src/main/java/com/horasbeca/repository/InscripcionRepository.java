package com.horasbeca.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.horasbeca.model.Inscripcion;

@Repository
public class InscripcionRepository {

    private final JdbcTemplate jdbcTemplate;

    public InscripcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // BUSCAR INSCRIPCION DE UN ESTUDIANTE
    public Optional<Inscripcion> buscar(
            long actividadId,
            String correoEstudiante) {

        String sql =
            "SELECT i.* FROM inscripciones i " +
            "JOIN usuarios u ON u.id = i.estudiante_id " +
            "WHERE i.actividad_id = ? " +
            "AND u.correo = ? " +
            "AND u.rol = 'ESTUDIANTE'";

        return jdbcTemplate.query(
            sql,
            (resultado, fila) -> new Inscripcion(
                resultado.getLong("id"),
                resultado.getLong("actividad_id"),
                resultado.getString("estado"),
                resultado.getString("inscrito_en"),
                resultado.getString("cancelado_en"),
                resultado.getString("motivo_cancelacion"),
                resultado.getInt("cancelada_tarde") == 1,
                resultado.getString("aviso_llegada_en"),
                resultado.getString("verificado_en")
            ),
            actividadId,
            correoEstudiante
        ).stream().findFirst();
    }

    // CONTAR CUPOS OCUPADOS
    public int ocupados(long actividadId) {

        Integer cantidad = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM inscripciones " +
            "WHERE actividad_id = ? " +
            "AND estado IN (" +
            "'INSCRITA', " +
            "'LLEGADA_AVISADA', " +
            "'LLEGADA_VERIFICADA')",
            Integer.class,
            actividadId
        );

        return cantidad == null ? 0 : cantidad;
    }

    // INSCRIBIR ESTUDIANTE
    public int inscribir(
            long actividadId,
            String correoEstudiante,
            LocalDateTime ahora) {

        String sql =
            "INSERT INTO inscripciones " +
            "(actividad_id, estudiante_id, estado, inscrito_en) " +
            "SELECT a.id, u.id, 'INSCRITA', ? " +
            "FROM actividades a " +
            "JOIN usuarios u ON u.correo = ? " +
            "AND u.rol = 'ESTUDIANTE' " +
            "JOIN perfiles_estudiantes p ON p.usuario_id = u.id " +
            "WHERE a.id = ? " +
            "AND (a.fecha > ? OR " +
            "(a.fecha = ? AND a.hora_inicio > ?)) " +
            "AND (SELECT COUNT(*) FROM inscripciones i " +
            "WHERE i.actividad_id = a.id " +
            "AND i.estado IN (" +
            "'INSCRITA', " +
            "'LLEGADA_AVISADA', " +
            "'LLEGADA_VERIFICADA')) < a.cupo_maximo " +
            "ON CONFLICT(actividad_id, estudiante_id) " +
            "DO UPDATE SET " +
            "estado = 'INSCRITA', " +
            "inscrito_en = excluded.inscrito_en, " +
            "cancelado_en = NULL, " +
            "motivo_cancelacion = NULL, " +
            "cancelada_tarde = 0, " +
            "aviso_llegada_en = NULL, " +
            "verificado_en = NULL " +
            "WHERE inscripciones.estado = 'CANCELADA'";

        String fecha = ahora.toLocalDate().toString();

        String hora = ahora.toLocalTime().format(
            DateTimeFormatter.ofPattern("HH:mm")
        );

        return jdbcTemplate.update(
            sql,
            ahora.toString(),
            correoEstudiante,
            actividadId,
            fecha,
            fecha,
            hora
        );
    }

    // CANCELAR INSCRIPCION Y LIBERAR CUPO
    public int cancelar(
            long actividadId,
            String correoEstudiante,
            LocalDateTime ahora,
            String motivo,
            boolean tardia) {

        String sql =
            "UPDATE inscripciones " +
            "SET estado = 'CANCELADA', " +
            "cancelado_en = ?, " +
            "motivo_cancelacion = ?, " +
            "cancelada_tarde = ? " +
            "WHERE actividad_id = ? " +
            "AND estudiante_id = (" +
            "SELECT id FROM usuarios " +
            "WHERE correo = ? AND rol = 'ESTUDIANTE') " +
            "AND estado = 'INSCRITA' " +
            "AND EXISTS (" +
            "SELECT 1 FROM actividades a " +
            "WHERE a.id = inscripciones.actividad_id " +
            "AND (a.fecha > ? OR " +
            "(a.fecha = ? AND a.hora_inicio > ?)))";

        String fecha = ahora.toLocalDate().toString();

        String hora = ahora.toLocalTime().format(
            DateTimeFormatter.ofPattern("HH:mm")
        );

        return jdbcTemplate.update(
            sql,
            ahora.toString(),
            motivo,
            tardia ? 1 : 0,
            actividadId,
            correoEstudiante,
            fecha,
            fecha,
            hora
        );
    }

    // GUARDAR HISTORIAL DE CANCELACIONES
    public int guardarCancelacion(
            long actividadId,
            String correoEstudiante,
            LocalDateTime ahora,
            String motivo,
            boolean tardia) {

        String sql =
            "INSERT INTO historial_cancelaciones " +
            "(inscripcion_id, cancelado_en, motivo, tardia) " +
            "SELECT i.id, ?, ?, ? " +
            "FROM inscripciones i " +
            "JOIN usuarios u ON u.id = i.estudiante_id " +
            "WHERE i.actividad_id = ? " +
            "AND u.correo = ? " +
            "AND i.estado = 'CANCELADA'";

        return jdbcTemplate.update(
            sql,
            ahora.toString(),
            motivo,
            tardia ? 1 : 0,
            actividadId,
            correoEstudiante
        );
    }
}
