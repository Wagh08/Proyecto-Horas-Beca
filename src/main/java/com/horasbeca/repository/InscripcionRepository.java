
package com.horasbeca.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.horasbeca.model.Inscripcion;
import com.horasbeca.model.Participante;
import com.horasbeca.model.Cancelacion;

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

    // LISTAR ACTIVIDADES CON INSCRIPCION ACTIVA
    public List<Long> listarIdsActivos(String correoEstudiante) {

        String sql =
            "SELECT i.actividad_id " +
            "FROM inscripciones i " +
            "JOIN usuarios u ON u.id = i.estudiante_id " +
            "WHERE u.correo = ? " +
            "AND i.estado IN (" +
            "'INSCRITA', " +
            "'LLEGADA_AVISADA', " +
            "'LLEGADA_VERIFICADA')";

        return jdbcTemplate.queryForList(
            sql,
            Long.class,
            correoEstudiante
        );
    }

    // LISTAR PARTICIPANTES DE UNA ACTIVIDAD
    public List<Participante> listarParticipantes(long actividadId) {

        String sql =
            "SELECT i.id, p.nombre, p.carne, i.estado, " +
            "i.aviso_llegada_en, i.verificado_en " +
            "FROM inscripciones i " +
            "JOIN perfiles_estudiantes p " +
            "ON p.usuario_id = i.estudiante_id " +
            "WHERE i.actividad_id = ? " +
            "AND i.estado <> 'CANCELADA' " +
            "ORDER BY i.inscrito_en, i.id";

        return jdbcTemplate.query(
            sql,
            (resultado, fila) -> new Participante(
                resultado.getLong("id"),
                resultado.getString("nombre"),
                resultado.getString("carne"),
                resultado.getString("estado"),
                resultado.getString("aviso_llegada_en"),
                resultado.getString("verificado_en")
            ),
            actividadId
        );
    }

    // LISTAR HISTORIAL DE CANCELACIONES
    public List<Cancelacion> listarCancelaciones(long actividadId) {

        String sql =
            "SELECT p.nombre, p.carne, h.cancelado_en, " +
            "h.motivo, h.tardia " +
            "FROM historial_cancelaciones h " +
            "JOIN inscripciones i " +
            "ON i.id = h.inscripcion_id " +
            "JOIN perfiles_estudiantes p " +
            "ON p.usuario_id = i.estudiante_id " +
            "WHERE i.actividad_id = ? " +
            "ORDER BY h.cancelado_en DESC, h.id DESC";

        return jdbcTemplate.query(
            sql,
            (resultado, fila) -> new Cancelacion(
                resultado.getString("nombre"),
                resultado.getString("carne"),
                resultado.getString("cancelado_en"),
                resultado.getString("motivo"),
                resultado.getInt("tardia") == 1
            ),
            actividadId
        );
    }

    // CONTAR CANCELACIONES TARDIAS
    public int contarCancelacionesTardias(long actividadId) {

        String sql =
            "SELECT COUNT(*) " +
            "FROM historial_cancelaciones h " +
            "JOIN inscripciones i " +
            "ON i.id = h.inscripcion_id " +
            "WHERE i.actividad_id = ? " +
            "AND h.tardia = 1";

        Integer cantidad = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            actividadId
        );

        return cantidad == null ? 0 : cantidad;
    }
}
