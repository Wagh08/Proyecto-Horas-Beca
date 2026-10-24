package com.horasbeca.security;

import com.horasbeca.model.Usuario;
import com.horasbeca.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServicioDetallesUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(usuario.correo())
            .password(usuario.contrasenaHash())
            .roles(usuario.rol())
            .build();
    }
}