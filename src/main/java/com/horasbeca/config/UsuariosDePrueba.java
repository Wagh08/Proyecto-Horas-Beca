package com.horasbeca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Profile("dev")
public class UsuariosDePrueba {

    @Bean
    CommandLineRunner cargarUsuariosDePrueba(
            JdbcTemplate jdbcTemplate) {

        return args -> {

            BCryptPasswordEncoder codificador =
                    new BCryptPasswordEncoder();

            insertarSiNoExiste(
                jdbcTemplate,
                codificador,
                "estudiante@prueba.com",
                "ESTUDIANTE"
            );

            insertarSiNoExiste(
                jdbcTemplate,
                codificador,
                "organizador@prueba.com",
                "ORGANIZADOR"
            );

            insertarPerfilEstudianteSiNoExiste(jdbcTemplate);
            insertarPerfilOrganizadorSiNoExiste(jdbcTemplate);

            Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuarios",
                Integer.class
            );

            System.out.println(
                "Usuarios registrados en SQLite: " + total
            );
        };
    }

    private void insertarSiNoExiste(
            JdbcTemplate jdbcTemplate,
            BCryptPasswordEncoder codificador,
            String correo,
            String rol) {

        Integer existentes = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM usuarios WHERE correo = ?",
            Integer.class,
            correo
        );

        if (existentes != null && existentes > 0) {
            return;
        }

        String contrasenaHash =
                codificador.encode("Prueba123!");

        jdbcTemplate.update(
            "INSERT INTO usuarios " +
            "(correo, contrasena_hash, rol) " +
            "VALUES (?, ?, ?)",
            correo,
            contrasenaHash,
            rol
        );
    }
    private void insertarPerfilEstudianteSiNoExiste(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(
            "INSERT INTO perfiles_estudiantes (usuario_id, nombre, carne, ciclo_academico, porcentaje_beca, telefono) SELECT id, ?, ?, ?, ?, ? FROM usuarios WHERE correo = ? AND NOT EXISTS (SELECT 1 FROM perfiles_estudiantes WHERE usuario_id = usuarios.id)",
            "Andrea López", "12345", 3, 20, null, "estudiante@prueba.com"
        );
    }

    private void insertarPerfilOrganizadorSiNoExiste(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(
            "INSERT INTO perfiles_organizadores (usuario_id, nombre, departamento, telefono) SELECT id, ?, ?, ? FROM usuarios WHERE correo = ? AND NOT EXISTS (SELECT 1 FROM perfiles_organizadores WHERE usuario_id = usuarios.id)",
            "Daniela Ruiz", "Biblioteca", null, "organizador@prueba.com"
        );
    }
}