
package com.horasbeca.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // INSCRIBIR ESTUDIANTE EN UNA ACTIVIDAD
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

    // CANCELAR INSCRIPCION Y LIBERAR CUPO
    @PostMapping("/actividades/{id}/cancelar")
    public String cancelar(
            @PathVariable("id") long id,
            @RequestParam(name = "motivo", defaultValue = "")
            String motivo,
            Authentication autenticacion) {

        try {
            inscripcionService.cancelar(
                id,
                autenticacion.getName(),
                motivo
            );

            return "redirect:/detalle-actividad.html?id="
                + id + "&cancelada";

        } catch (IllegalArgumentException excepcion) {

            return "redirect:/detalle-actividad.html?id="
                + id + "&errorCancelacion";
        }
    }
}
