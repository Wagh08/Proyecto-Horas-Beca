package com.horasbeca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

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
                .requestMatchers(HttpMethod.POST, "/perfil-estudiante/contacto").hasRole("ESTUDIANTE")
                .requestMatchers(HttpMethod.POST, "/perfil-organizador/contacto").hasRole("ORGANIZADOR")
                .requestMatchers(HttpMethod.POST, "/actividades/publicar").hasRole("ORGANIZADOR")
                .requestMatchers(HttpMethod.GET, "/api/ubicaciones").hasRole("ESTUDIANTE")
                .anyRequest().denyAll()
        
            )
            .formLogin(formulario -> formulario
                .loginPage("/login")
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