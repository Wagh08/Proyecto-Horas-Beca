package com.horasbeca.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

@Controller
public class PantallasController {

    private static final Set<String> PAGINAS = Set.of(
        "inicio-estudiante",
        "actividades",
        "detalle-actividad",
        "asistencia-actividad",
        "qr-actividad",
        "progreso",
        "perfil-estudiante",
        "inicio-organizador",
        "gestion",
        "crear-actividad",
        "participantes",
        "solicitudes",
        "verificar-llegadas",
        "escanear-qr",
        "perfil-organizador"
    );

    @GetMapping("/")
    public String inicio(Authentication autenticacion) {
        boolean esOrganizador = autenticacion.getAuthorities().stream()
            .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ORGANIZADOR"));

        return esOrganizador
            ? "redirect:/inicio-organizador.html"
            : "redirect:/inicio-estudiante.html";
    }

    @GetMapping("/{pagina}.html")
    public String mostrarPagina(@PathVariable("pagina") String pagina) {
        if (!PAGINAS.contains(pagina)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return pagina;
    }
}