
package com.horasbeca.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CodigoQrRepository {

    private final JdbcTemplate jdbcTemplate;

    public CodigoQrRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // BUSCAR EL TOKEN QR DE UNA INSCRIPCION
    public Optional<String> buscarToken(long inscripcionId) {

        String sql =
            "SELECT token FROM codigos_qr " +
            "WHERE inscripcion_id = ?";

        return jdbcTemplate.query(
            sql,
            (resultado, fila) -> resultado.getString("token"),
            inscripcionId
        ).stream().findFirst();
    }

    // CREAR CODIGO QR SI TODAVIA NO EXISTE
    public void crearSiFalta(
            long inscripcionId,
            String correoEstudiante,
            String token,
            LocalDateTime ahora) {

        String sql =
            "INSERT OR IGNORE INTO codigos_qr " +
            "(inscripcion_id, token, creado_en) " +
            "SELECT i.id, ?, ? " +
            "FROM inscripciones i " +
            "JOIN usuarios u ON u.id = i.estudiante_id " +
            "JOIN actividades a ON a.id = i.actividad_id " +
            "WHERE i.id = ? " +
            "AND u.correo = ? " +
            "AND u.rol = 'ESTUDIANTE' " +
            "AND i.estado = 'LLEGADA_VERIFICADA' " +
            "AND i.aviso_llegada_en IS NOT NULL " +
            "AND i.verificado_en IS NOT NULL " +
            "AND (a.fecha < ? OR " +
            "(a.fecha = ? AND a.hora_fin <= ?))";

        String fecha = ahora.toLocalDate().toString();

        String hora = ahora.toLocalTime().format(
            DateTimeFormatter.ofPattern("HH:mm")
        );

        int insertados = jdbcTemplate.update(
            sql,
            token,
            ahora.toString(),
            inscripcionId,
            correoEstudiante,
            fecha,
            fecha,
            hora
        );

        if (insertados == 0 && buscarToken(inscripcionId).isEmpty()) {
            throw new IllegalArgumentException(
                "No se pudo generar el QR para esta inscripción."
            );
        }
    }
}
