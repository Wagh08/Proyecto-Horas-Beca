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
}