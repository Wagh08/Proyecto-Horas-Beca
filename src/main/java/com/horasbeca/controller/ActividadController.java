package com.horasbeca.controller;

import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.service.ActividadService;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.horasbeca.model.Actividad;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ActividadController {

    private final ActividadRepository actividadRepository;
    private final ActividadService actividadService;

    public ActividadController(ActividadRepository actividadRepository, ActividadService actividadService) {
        this.actividadRepository = actividadRepository;
        this.actividadService = actividadService;
    }

    @GetMapping("/crear-actividad.html")
    public String formulario(Model modelo) {
        modelo.addAttribute("valores", Map.of());
        return "crear-actividad";
    }

    @PostMapping("/actividades/publicar")
    public String publicar(@RequestParam Map<String, String> campos, Authentication autenticacion, Model modelo) {
        try {
            actividadService.publicar(campos, autenticacion.getName());
            return "redirect:/gestion.html?publicada";
        } catch (IllegalArgumentException excepcion) {
            modelo.addAttribute("error", excepcion.getMessage());
            modelo.addAttribute("valores", campos);
            return "crear-actividad";
        }
    }

    @GetMapping("/gestion.html")
    public String gestion(Authentication autenticacion, Model modelo) {
        modelo.addAttribute("actividades", actividadRepository.listarPropias(autenticacion.getName()));
        return "gestion";
    }

    @GetMapping("/actividades.html")
    public String actividades(Model modelo) {
        modelo.addAttribute("actividades", actividadRepository.listarDisponibles());
        return "actividades";
    }

    @GetMapping("/detalle-actividad.html")
    public String detalle(@RequestParam(name = "id", required = false) Long id, Model modelo) {
        if (id == null || id < 1) {
            return "redirect:/actividades.html";
        }

        Actividad actividad = actividadRepository.buscarDisponiblePorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no disponible"));

        modelo.addAttribute("actividad", actividad);
        return "detalle-actividad";
    }
}