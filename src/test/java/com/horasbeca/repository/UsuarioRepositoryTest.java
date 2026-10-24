package com.horasbeca.repository;

import com.horasbeca.model.Usuario;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void encuentraEstudiantePorCorreo() {
        Optional<Usuario> resultado = usuarioRepository.buscarPorCorreo("estudiante@prueba.com");

        assertTrue(resultado.isPresent());
        assertEquals("ESTUDIANTE", resultado.get().rol());
        assertFalse(resultado.get().contrasenaHash().isBlank());
    }

    @Test
    void encuentraOrganizadorAunqueElCorreoTengaMayusculasYEspacios() {
        Optional<Usuario> resultado = usuarioRepository.buscarPorCorreo("  ORGANIZADOR@PRUEBA.COM  ");

        assertTrue(resultado.isPresent());
        assertEquals("ORGANIZADOR", resultado.get().rol());
    }

    @Test
    void devuelveVacioSiElCorreoNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.buscarPorCorreo("nadie@prueba.com");

        assertTrue(resultado.isEmpty());
    }
}