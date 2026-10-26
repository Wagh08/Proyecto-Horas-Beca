
package com.horasbeca.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
import com.horasbeca.model.Inscripcion;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;
import com.horasbeca.service.InscripcionService;

@Controller
public class InscripcionController {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

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

    // RF-06: INSCRIBIR ESTUDIANTE EN UNA ACTIVIDAD
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

    // RF-06: CANCELAR INSCRIPCION Y LIBERAR CUPO
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

    // RF-07.01: MOSTRAR ESTADO DE ASISTENCIA
    @GetMapping("/asistencia-actividad.html")
    public String asistencia(
            @RequestParam(name = "id", required = false)
            Long id,
            Authentication autenticacion,
            Model modelo) {

        if (id == null || id < 1) {
            return "redirect:/actividades.html";
        }

        // BUSCAR ACTIVIDAD
        Actividad actividad = actividadRepository
            .buscarPorId(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actividad no encontrada"
                )
            );

        // COMPROBAR INSCRIPCION ACTIVA
        Inscripcion inscripcion = inscripcionRepository
            .buscar(id, autenticacion.getName())
            .filter(registro ->
                !registro.estado().equals("CANCELADA")
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No tienes una inscripción activa"
                )
            );

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        LocalDateTime inicio = LocalDateTime.of(
            actividad.fecha(),
            actividad.horaInicio()
        );

        LocalDateTime fin = LocalDateTime.of(
            actividad.fecha(),
            actividad.horaFin()
        );

        // SOLO PERMITIR AVISAR DURANTE EL HORARIO
        boolean puedeAvisar =
            inscripcion.estado().equals("INSCRITA")
            && !ahora.isBefore(inicio)
            && ahora.isBefore(fin);

        modelo.addAttribute("actividad", actividad);
        modelo.addAttribute("inscripcion", inscripcion);
        modelo.addAttribute("puedeAvisar", puedeAvisar);

        return "asistencia-actividad";
    }

    // RF-07.01: REGISTRAR AVISO DE LLEGADA
    @PostMapping("/actividades/{id}/avisar-llegada")
    public String avisarLlegada(
            @PathVariable("id") long id,
            Authentication autenticacion) {

        try {
            inscripcionService.avisarLlegada(
                id,
                autenticacion.getName()
            );

            return "redirect:/asistencia-actividad.html?id="
                + id + "&avisoGuardado";

        } catch (IllegalArgumentException excepcion) {

            return "redirect:/asistencia-actividad.html?id="
                + id + "&errorAviso";
        }
    }
}
