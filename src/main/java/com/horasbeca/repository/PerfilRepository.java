package com.horasbeca.repository;

import com.horasbeca.model.PerfilEstudiante;
import com.horasbeca.model.PerfilOrganizador;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PerfilRepository {

    private final JdbcTemplate jdbcTemplate;

    public PerfilRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PerfilEstudiante> buscarEstudiantePorCorreo(String correo) {
        return jdbcTemplate.query(
            "SELECT u.correo, e.nombre, e.carne, e.ciclo_academico, e.porcentaje_beca, e.telefono FROM perfiles_estudiantes e JOIN usuarios u ON u.id = e.usuario_id WHERE u.correo = ?",
            (resultado, fila) -> new PerfilEstudiante(
                resultado.getString("nombre"),
                resultado.getString("correo"),
                resultado.getString("carne"),
                resultado.getInt("ciclo_academico"),
                resultado.getInt("porcentaje_beca"),
                resultado.getString("telefono")
            ),
            correo
        ).stream().findFirst();
    }

    public Optional<PerfilOrganizador> buscarOrganizadorPorCorreo(String correo) {
        return jdbcTemplate.query(
            "SELECT u.correo, o.nombre, o.departamento, o.telefono FROM perfiles_organizadores o JOIN usuarios u ON u.id = o.usuario_id WHERE u.correo = ?",
            (resultado, fila) -> new PerfilOrganizador(
                resultado.getString("nombre"),
                resultado.getString("correo"),
                resultado.getString("departamento"),
                resultado.getString("telefono")
            ),
            correo
        ).stream().findFirst();
    }
}