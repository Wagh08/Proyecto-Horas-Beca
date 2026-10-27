package com.horasbeca.repository;

import com.horasbeca.model.RegistroHoras;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public class AcreditacionRepository {

    private final JdbcTemplate jdbcTemplate;

    public AcreditacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registrarSiValida(long actividadId, String correoOrganizador, String token, LocalDateTime ahora) {
        String sql = "INSERT INTO acreditaciones (inscripcion_id, horas_acreditadas, acreditado_en) "
            + "SELECT i.id, a.horas_acreditables, ? "
            + "FROM codigos_qr c "
            + "JOIN inscripciones i ON i.id = c.inscripcion_id "
            + "JOIN actividades a ON a.id = i.actividad_id "
            + "JOIN usuarios u ON u.id = a.organizador_id "
            + "WHERE c.token = ? AND a.id = ? AND u.correo = ? AND u.rol = 'ORGANIZADOR' "
            + "AND i.estado = 'LLEGADA_VERIFICADA' "
            + "AND i.aviso_llegada_en IS NOT NULL AND i.verificado_en IS NOT NULL "
            + "AND (a.fecha < ? OR (a.fecha = ? AND a.hora_fin <= ?)) "
            + "AND NOT EXISTS (SELECT 1 FROM acreditaciones ac WHERE ac.inscripcion_id = i.id) "
            + "ON CONFLICT(inscripcion_id) DO NOTHING";

        String fecha = ahora.toLocalDate().toString();
        String hora = ahora.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return jdbcTemplate.update(sql, ahora.toString(), token, actividadId, correoOrganizador, fecha, fecha, hora);
    }

    public boolean existe(long inscripcionId) {
        Integer cantidad = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM acreditaciones WHERE inscripcion_id = ?", Integer.class, inscripcionId);
        return cantidad != null && cantidad > 0;
    }

    public int totalAnual(String correoEstudiante, int anio) {
        String sql = "SELECT COALESCE(SUM(ac.horas_acreditadas), 0) "
            + "FROM acreditaciones ac "
            + "JOIN inscripciones i ON i.id = ac.inscripcion_id "
            + "JOIN usuarios u ON u.id = i.estudiante_id "
            + "WHERE u.correo = ? AND u.rol = 'ESTUDIANTE' "
            + "AND ac.acreditado_en >= ? AND ac.acreditado_en < ?";

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, correoEstudiante, anio + "-01-01", (anio + 1) + "-01-01");
        return total == null ? 0 : total;
    }

    public List<RegistroHoras> listarAnuales(String correoEstudiante, int anio) {
        String sql = "SELECT a.nombre AS actividad, po.nombre AS organizador, ac.acreditado_en, ac.horas_acreditadas "
            + "FROM acreditaciones ac "
            + "JOIN inscripciones i ON i.id = ac.inscripcion_id "
            + "JOIN actividades a ON a.id = i.actividad_id "
            + "JOIN perfiles_organizadores po ON po.usuario_id = a.organizador_id "
            + "JOIN usuarios u ON u.id = i.estudiante_id "
            + "WHERE u.correo = ? AND u.rol = 'ESTUDIANTE' "
            + "AND ac.acreditado_en >= ? AND ac.acreditado_en < ? "
            + "ORDER BY ac.acreditado_en DESC, ac.id DESC";

        return jdbcTemplate.query(sql, (resultado, fila) -> new RegistroHoras(
            resultado.getString("actividad"),
            resultado.getString("organizador"),
            resultado.getString("acreditado_en"),
            resultado.getInt("horas_acreditadas")
        ), correoEstudiante, anio + "-01-01", (anio + 1) + "-01-01");
    }
    public List<Integer> listarAnios(String correoEstudiante, int anioActual) {
        String sql = "SELECT DISTINCT CAST(SUBSTR(ac.acreditado_en, 1, 4) AS INTEGER) AS anio "
            + "FROM acreditaciones ac "
            + "JOIN inscripciones i ON i.id = ac.inscripcion_id "
            + "JOIN usuarios u ON u.id = i.estudiante_id "
            + "WHERE u.correo = ? AND u.rol = 'ESTUDIANTE' "
            + "AND ac.acreditado_en < ? "
            + "ORDER BY anio DESC";

        List<Integer> anios = new ArrayList<>();
        anios.add(anioActual);

        for (Integer anio : jdbcTemplate.queryForList(sql, Integer.class, correoEstudiante, (anioActual + 1) + "-01-01")) {
            if (!anio.equals(anioActual)) {
                anios.add(anio);
            }
        }

        return anios;
    }
}