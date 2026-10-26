package com.horasbeca.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;
import com.horasbeca.service.InscripcionService;

@Controller
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final InscripcionRepository inscripcionRepository;
    private final ActividadRepository actividadRepository;

    public InscripcionController(
            InscripcionService inscripcionService,
            InscripcionRepository inscripcionRepository,
            ActividadRepository actividadRepository) {

        this.inscripcionService = inscripcionService;
        this.inscripcionRepository = inscripcionRepository;
        this.actividadRepository = actividadRepository;
    }

    @PostMapping("/actividades/{id}/inscribir")
    public String inscribir(
            @PathVariable("id") long id,
            Authentication autenticacion) {

        try {
            inscripcionService.inscribir(
                id,
                autenticacion.getName()
            );

            return "redirect:/detalle-actividad.html?id="
                + id + "&inscrita";

        } catch (IllegalArgumentException excepcion) {

            return "redirect:/detalle-actividad.html?id="
                + id + "&errorInscripcion";
        }
    }
}
