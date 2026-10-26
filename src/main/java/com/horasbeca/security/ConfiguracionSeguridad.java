
package com.horasbeca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(autorizar -> autorizar

                // ARCHIVOS PUBLICOS
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/imgs/**",
                    "/favicon.ico",
                    "/error"
                ).permitAll()

                // PAGINA PRINCIPAL
                .requestMatchers("/")
                .authenticated()

                // PAGINAS DEL ESTUDIANTE
                .requestMatchers(
                    "/inicio-estudiante.html",
                    "/actividades.html",
                    "/detalle-actividad.html",
                    "/asistencia-actividad.html",
                    "/qr-actividad.html",
                    "/progreso.html",
                    "/perfil-estudiante.html"
                ).hasRole("ESTUDIANTE")

                // PAGINAS DEL ORGANIZADOR
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

                // ACTUALIZAR CONTACTO DEL ESTUDIANTE
                .requestMatchers(
                    HttpMethod.POST,
                    "/perfil-estudiante/contacto"
                ).hasRole("ESTUDIANTE")

                // ACTUALIZAR CONTACTO DEL ORGANIZADOR
                .requestMatchers(
                    HttpMethod.POST,
                    "/perfil-organizador/contacto"
                ).hasRole("ORGANIZADOR")

                // PUBLICAR ACTIVIDADES
                .requestMatchers(
                    HttpMethod.POST,
                    "/actividades/publicar"
                ).hasRole("ORGANIZADOR")

                // CONSULTAR UBICACIONES
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/ubicaciones"
                ).hasRole("ESTUDIANTE")

                // RF-06: INSCRIBIRSE EN UNA ACTIVIDAD
                .requestMatchers(
                    HttpMethod.POST,
                    "/actividades/*/inscribir"
                ).hasRole("ESTUDIANTE")

                // RF-06: CANCELAR INSCRIPCION
                .requestMatchers(
                    HttpMethod.POST,
                    "/actividades/*/cancelar"
                ).hasRole("ESTUDIANTE")

                // RF-07.01: AVISAR LLEGADA
                .requestMatchers(
                    HttpMethod.POST,
                    "/actividades/*/avisar-llegada"
                ).hasRole("ESTUDIANTE")

                // RF-07.03: VERIFICAR LLEGADA
                .requestMatchers(
                    HttpMethod.POST,
                    "/inscripciones/*/verificar-llegada"
                ).hasRole("ORGANIZADOR")

                // DENEGAR RUTAS NO AUTORIZADAS
                .anyRequest().denyAll()
            )

            // INICIO DE SESION
            .formLogin(formulario -> formulario
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            // CERRAR SESION
            .logout(salida -> salida
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
