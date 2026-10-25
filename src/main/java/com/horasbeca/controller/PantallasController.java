package com.horasbeca.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import com.horasbeca.model.PerfilEstudiante;
import com.horasbeca.repository.PerfilRepository;
import com.horasbeca.service.MetaAnualService;
import org.springframework.ui.Model;

@Controller
public class PantallasController {

    private static final Set<String> PAGINAS = Set.of(
        "inicio-estudiante",
        "asistencia-actividad",
        "qr-actividad",
        "progreso",
        "perfil-estudiante",
        "inicio-organizador",
        "participantes",
        "solicitudes",
        "verificar-llegadas",
        "escanear-qr",
        "perfil-organizador"
    );

    private final PerfilRepository perfilRepository;
    private final MetaAnualService metaAnualService;

    public PantallasController(PerfilRepository perfilRepository, MetaAnualService metaAnualService) {
        this.perfilRepository = perfilRepository;
        this.metaAnualService = metaAnualService;
    }

    @GetMapping("/")
    public String inicio(Authentication autenticacion) {
        boolean esOrganizador = autenticacion.getAuthorities().stream()
            .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ORGANIZADOR"));

        return esOrganizador
            ? "redirect:/inicio-organizador.html"
            : "redirect:/inicio-estudiante.html";
    }

    @GetMapping("/{pagina}.html")
    public String mostrarPagina(@PathVariable("pagina") String pagina, Authentication autenticacion, Model modelo) {
        if (!PAGINAS.contains(pagina)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (pagina.equals("perfil-estudiante")) {
            PerfilEstudiante perfil = perfilRepository.buscarEstudiantePorCorreo(autenticacion.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de estudiante no encontrado"));

            modelo.addAttribute("perfil", perfil);
            modelo.addAttribute("metaAnual", metaAnualService.calcularHorasRequeridas(perfil.porcentajeBeca()));
        }

        if (pagina.equals("perfil-organizador")) {
            modelo.addAttribute("perfil", perfilRepository.buscarOrganizadorPorCorreo(autenticacion.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de organizador no encontrado")));
        }

        return pagina;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}