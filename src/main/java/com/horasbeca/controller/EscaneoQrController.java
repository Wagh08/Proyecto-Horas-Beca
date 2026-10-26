
package com.horasbeca.controller;

import com.horasbeca.model.Actividad;
import com.horasbeca.repository.ActividadRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class EscaneoQrController {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final ActividadRepository actividadRepository;

    public EscaneoQrController(
            ActividadRepository actividadRepository) {

        this.actividadRepository = actividadRepository;
    }

    // MOSTRAR LECTOR QR DEL ORGANIZADOR
    @GetMapping("/escanear-qr.html")
    public String mostrar(
            @RequestParam("id") long id,
            Authentication autenticacion,
            Model modelo) {

        // COMPROBAR QUE LA ACTIVIDAD
        // PERTENECE AL ORGANIZADOR
        Actividad actividad =
            actividadRepository.buscarPropiaPorId(
                id,
                autenticacion.getName()
            ).orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actividad no encontrada"
                )
            );

        // COMPROBAR SI LA ACTIVIDAD TERMINO
        boolean finalizada =
            !LocalDateTime.now(ZONA).isBefore(
                LocalDateTime.of(
                    actividad.fecha(),
                    actividad.horaFin()
                )
            );

        modelo.addAttribute("actividad", actividad);
        modelo.addAttribute("finalizada", finalizada);

        return "escanear-qr";
    }
}
