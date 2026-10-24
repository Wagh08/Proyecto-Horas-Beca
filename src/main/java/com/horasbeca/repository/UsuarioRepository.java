package com.horasbeca.repository;

import com.horasbeca.model.Usuario;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return Optional.empty();
        }

        String correoNormalizado = correo.trim().toLowerCase(Locale.ROOT);

        List<Usuario> usuarios = jdbcTemplate.query(
            "SELECT id, correo, contrasena_hash, rol FROM usuarios WHERE correo = ? LIMIT 1",
            (resultado, fila) -> new Usuario(
                resultado.getLong("id"),
                resultado.getString("correo"),
                resultado.getString("contrasena_hash"),
                resultado.getString("rol")
            ),
            correoNormalizado
        );

        return usuarios.stream().findFirst();
    }
}