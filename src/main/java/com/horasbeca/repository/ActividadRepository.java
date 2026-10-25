
package com.horasbeca.repository;

import com.horasbeca.model.Actividad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActividadRepository {

    private static final String CONSULTA =
        "SELECT a.id, a.nombre, a.descripcion, a.requisitos, " +
        "a.ubicacion, a.fecha, a.hora_inicio, a.hora_fin, " +
        "a.cupo_maximo, a.horas_acreditables, " +
        "p.nombre AS organizador " +
        "FROM actividades a " +
        "JOIN perfiles_organizadores p ON p.usuario_id = a.organizador_id";

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final JdbcTemplate jdbcTemplate;

    public ActividadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    
    public int guardar(Actividad actividad, String correoOrganizador) {
        return jdbcTemplate.update(
            "INSERT INTO actividades " +
            "(organizador_id, nombre, descripcion, requisitos, " +
            "ubicacion, fecha, hora_inicio, hora_fin, " +
            "cupo_maximo, horas_acreditables) " +
            "SELECT u.id, ?, ?, ?, ?, ?, ?, ?, ?, ? " +
            "FROM usuarios u " +
            "JOIN perfiles_organizadores p ON p.usuario_id = u.id " +
            "WHERE u.correo = ? AND u.rol = 'ORGANIZADOR'",
            actividad.nombre(),
            actividad.descripcion(),
            actividad.requisitos(),
            actividad.ubicacion(),
            actividad.fecha().toString(),
            actividad.horaInicio().toString(),
            actividad.horaFin().toString(),
            actividad.cupoMaximo(),
            actividad.horasAcreditables(),
            correoOrganizador
        );
    }

    
    public List<Actividad> listarPropias(String correoOrganizador) {
        return jdbcTemplate.query(
            CONSULTA +
            " JOIN usuarios u ON u.id = a.organizador_id " +
            "WHERE u.correo = ? " +
            "ORDER BY a.fecha DESC, a.hora_inicio DESC, a.id DESC",
            this::mapear,
            correoOrganizador
        );
    }

    
    public List<Actividad> listarDisponibles() {
        return listarDisponibles(null, "", null);
    }

    
    public List<Actividad> listarDisponibles(
            LocalDate fecha,
            String turno,
            Integer horasMinimas) {

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        StringBuilder consulta = new StringBuilder(
            CONSULTA +
            " WHERE (a.fecha > ? OR " +
            "(a.fecha = ? AND a.hora_inicio > ?))"
        );

        List<Object> parametros = new ArrayList<>();

        parametros.add(ahora.toLocalDate().toString());
        parametros.add(ahora.toLocalDate().toString());
        parametros.add(
            ahora.toLocalTime().format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
        );

        if (fecha != null) {
            consulta.append(" AND a.fecha = ?");
            parametros.add(fecha.toString());
        }

        if (horasMinimas != null) {
            consulta.append(" AND a.horas_acreditables >= ?");
            parametros.add(horasMinimas);
        }

        if ("manana".equals(turno)) {
            consulta.append(" AND a.hora_inicio < '12:00'");
        }

        if ("tarde".equals(turno)) {
            consulta.append(" AND a.hora_inicio >= '12:00'");
        }

        consulta.append(
            " ORDER BY a.fecha, a.hora_inicio, a.id"
        );

        return jdbcTemplate.query(
            consulta.toString(),
            this::mapear,
            parametros.toArray()
        );
    }

    
    public Optional<Actividad> buscarDisponiblePorId(long id) {

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        return jdbcTemplate.query(
            CONSULTA +
            " WHERE a.id = ? " +
            "AND (a.fecha > ? OR " +
            "(a.fecha = ? AND a.hora_inicio > ?))",
            this::mapear,
            id,
            ahora.toLocalDate().toString(),
            ahora.toLocalDate().toString(),
            ahora.toLocalTime().format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
        ).stream().findFirst();
    }

    
    private Actividad mapear(ResultSet resultado, int fila)
            throws SQLException {

        return new Actividad(
            resultado.getLong("id"),
            resultado.getString("nombre"),
            resultado.getString("descripcion"),
            resultado.getString("requisitos"),
            resultado.getString("ubicacion"),
            LocalDate.parse(resultado.getString("fecha")),
            LocalTime.parse(resultado.getString("hora_inicio")),
            LocalTime.parse(resultado.getString("hora_fin")),
            resultado.getInt("cupo_maximo"),
            resultado.getInt("horas_acreditables"),
            resultado.getString("organizador")
        );
    }
}

