
package com.horasbeca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.horasbeca.model.Actividad;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;
import com.horasbeca.service.InscripcionService;

@Controller
public class LlegadasController {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final InscripcionService inscripcionService;

    public LlegadasController(
            ActividadRepository actividadRepository,
            InscripcionRepository inscripcionRepository,
            InscripcionService inscripcionService) {

        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.inscripcionService = inscripcionService;
    }

    // RF-07.02: MOSTRAR LISTA DE LLEGADAS
    @GetMapping("/verificar-llegadas.html")
    public String listar(
            @RequestParam(name = "id", required = false) Long id,
            Authentication autenticacion,
            Model modelo) {

        if (id == null || id < 1) {
            return "redirect:/gestion.html";
        }

        // COMPROBAR QUE LA ACTIVIDAD PERTENECE AL ORGANIZADOR
        Actividad actividad = actividadRepository
            .buscarPropiaPorId(id, autenticacion.getName())
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actividad no encontrada"
                )
            );

        // CARGAR ACTIVIDAD Y PARTICIPANTES REALES
        modelo.addAttribute("actividad", actividad);

        modelo.addAttribute(
            "participantes",
            inscripcionRepository.listarParticipantes(id)
        );

        return "verificar-llegadas";
    }

    // RF-07.03: VERIFICAR LLEGADA
    @PostMapping("/inscripciones/{id}/verificar-llegada")
    public String verificar(
            @PathVariable("id") long id,
            @RequestParam("actividadId") long actividadId,
            Authentication autenticacion) {

        try {
            inscripcionService.verificarLlegada(
                id,
                actividadId,
                autenticacion.getName()
            );

            return "redirect:/verificar-llegadas.html?id="
                + actividadId + "&verificada";

        } catch (IllegalArgumentException excepcion) {

            return "redirect:/verificar-llegadas.html?id="
                + actividadId + "&errorVerificacion";
        }
    }
}

