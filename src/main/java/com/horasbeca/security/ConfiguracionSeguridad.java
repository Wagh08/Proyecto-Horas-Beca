package com.horasbeca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(autorizar -> autorizar
                .requestMatchers("/css/**", "/js/**", "/imgs/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/").authenticated()
                .requestMatchers(
                    "/inicio-estudiante.html",
                    "/actividades.html",
                    "/detalle-actividad.html",
                    "/asistencia-actividad.html",
                    "/qr-actividad.html",
                    "/progreso.html",
                    "/perfil-estudiante.html"
                ).hasRole("ESTUDIANTE")
                .requestMatchers(
                    "/inicio-organizador.html",
                    "/gestion.html",
                    "/crear-actividad.html",
                    "/participantes.html",
                    "/solicitudes.html",
                    "/verificar-llegadas.html",
                    "/escanear-qr.html",
                    "/perfil-organizador.html"
                ).hasRole("ORGANIZADOR")
                .anyRequest().denyAll()
            )
            .formLogin(formulario -> formulario
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(salida -> salida
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}