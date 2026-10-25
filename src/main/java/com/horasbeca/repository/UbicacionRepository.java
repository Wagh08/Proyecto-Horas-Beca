package com.horasbeca.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UbicacionRepository {

    private final JdbcTemplate jdbcTemplate;

    public UbicacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> listarUbicaciones() {
        String sql = "SELECT DISTINCT ubicacion FROM actividades WHERE ubicacion IS NOT NULL AND TRIM(ubicacion) <> '' ORDER BY ubicacion COLLATE NOCASE";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}