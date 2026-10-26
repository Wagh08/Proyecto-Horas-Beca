
package com.horasbeca.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.horasbeca.model.Actividad;
import com.horasbeca.model.Inscripcion;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;
import com.horasbeca.service.ActividadService;
import com.horasbeca.service.UbicacionService;

@Controller
public class ActividadController {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final ActividadRepository actividadRepository;
    private final ActividadService actividadService;
    private final UbicacionService ubicacionService;
    private final InscripcionRepository inscripcionRepository;

    public ActividadController(
            ActividadRepository actividadRepository,
            ActividadService actividadService,
            UbicacionService ubicacionService,
            InscripcionRepository inscripcionRepository) {

        this.actividadRepository = actividadRepository;
        this.actividadService = actividadService;
        this.ubicacionService = ubicacionService;
        this.inscripcionRepository = inscripcionRepository;
    }

    // MOSTRAR FORMULARIO PARA CREAR ACTIVIDAD
    @GetMapping("/crear-actividad.html")
    public String crearActividad(Model modelo) {
        modelo.addAttribute("valores", Map.of());
        return "crear-actividad";
    }

    // PUBLICAR ACTIVIDAD
    @PostMapping("/actividades/publicar")
    public String publicar(
            @RequestParam Map<String, String> campos,
            Authentication autenticacion,
            Model modelo) {

        try {
            actividadService.publicar(
                campos,
                autenticacion.getName()
            );

            return "redirect:/gestion.html?publicada";

        } catch (IllegalArgumentException excepcion) {

            modelo.addAttribute(
                "error",
                excepcion.getMessage()
            );

            modelo.addAttribute("valores", campos);

            return "crear-actividad";
        }
    }

    // GESTION DE ACTIVIDADES DEL ORGANIZADOR
    @GetMapping("/gestion.html")
    public String gestion(
            Authentication autenticacion,
            Model modelo) {

        List<Actividad> actividades =
            actividadRepository.listarPropias(
                autenticacion.getName()
            );

        Map<Long, Integer> alertas = new HashMap<>();

        for (Actividad actividad : actividades) {
            alertas.put(
                actividad.id(),
                inscripcionRepository
                    .contarCancelacionesTardias(
                        actividad.id()
                    )
            );
        }

        modelo.addAttribute("actividades", actividades);
        modelo.addAttribute("alertas", alertas);

        return "gestion";
    }

    // LISTAR ACTIVIDADES DISPONIBLES
    @GetMapping("/actividades.html")
    public String actividades(
            @RequestParam(name = "fecha", defaultValue = "")
            String fecha,

            @RequestParam(name = "turno", defaultValue = "")
            String turno,

            @RequestParam(name = "horasMinimas", defaultValue = "")
            String horasMinimas,

            @RequestParam(name = "ubicacion", defaultValue = "")
            String ubicacion,

            Authentication autenticacion,
            Model modelo) {

        modelo.addAttribute("fecha", fecha);
        modelo.addAttribute("turno", turno);
        modelo.addAttribute("horasMinimas", horasMinimas);
        modelo.addAttribute("ubicacion", ubicacion);

        List<Actividad> actividades;

        try {
            actividades = actividadService
                .filtrarDisponibles(
                    fecha,
                    turno,
                    horasMinimas
                );

        } catch (IllegalArgumentException excepcion) {

            modelo.addAttribute(
                "errorFiltros",
                excepcion.getMessage()
            );

            actividades =
                actividadRepository.listarDisponibles();
        }

        actividades = actividades.stream()
            .filter(actividad ->
                ubicacionService.coincide(
                    actividad.ubicacion(),
                    ubicacion
                )
            )
            .toList();

        Map<Long, Integer> cuposDisponibles =
            new HashMap<>();

        for (Actividad actividad : actividades) {

            cuposDisponibles.put(
                actividad.id(),
                Math.max(
                    0,
                    actividad.cupoMaximo()
                        - inscripcionRepository.ocupados(
                            actividad.id()
                        )
                )
            );
        }

        modelo.addAttribute(
            "actividades",
            actividades
        );

        modelo.addAttribute(
            "cuposDisponibles",
            cuposDisponibles
        );

        modelo.addAttribute(
            "idsInscritas",
            inscripcionRepository.listarIdsActivos(
                autenticacion.getName()
            )
        );

        return "actividades";
    }

    // DETALLE DE ACTIVIDAD E INSCRIPCION
    @GetMapping("/detalle-actividad.html")
    public String detalle(
            @RequestParam(name = "id", required = false)
            Long id,
            Authentication autenticacion,
            Model modelo) {

        if (id == null || id < 1) {
            return "redirect:/actividades.html";
        }

        Actividad actividad =
            actividadRepository.buscarPorId(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actividad no encontrada"
                    )
                );

        Inscripcion inscripcion =
            inscripcionRepository.buscar(
                id,
                autenticacion.getName()
            ).orElse(null);

        boolean noHaComenzado =
            LocalDateTime.now(ZONA).isBefore(
                LocalDateTime.of(
                    actividad.fecha(),
                    actividad.horaInicio()
                )
            );

        if (!noHaComenzado &&
                (inscripcion == null ||
                 inscripcion.estado().equals("CANCELADA"))) {

            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Actividad no disponible"
            );
        }

        modelo.addAttribute(
            "actividad",
            actividad
        );

        modelo.addAttribute(
            "inscripcion",
            inscripcion
        );

        modelo.addAttribute(
            "cuposDisponibles",
            Math.max(
                0,
                actividad.cupoMaximo()
                    - inscripcionRepository.ocupados(id)
            )
        );

        modelo.addAttribute(
            "noHaComenzado",
            noHaComenzado
        );

        return "detalle-actividad";
    }

    // MOSTRAR PARTICIPANTES Y CANCELACIONES
    @GetMapping("/participantes.html")
    public String participantes(
            @RequestParam(name = "id", required = false)
            Long id,
            Authentication autenticacion,
            Model modelo) {

        if (id == null || id < 1) {
            return "redirect:/gestion.html";
        }

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

        modelo.addAttribute(
            "actividad",
            actividad
        );

        modelo.addAttribute(
            "participantes",
            inscripcionRepository.listarParticipantes(id)
        );

        modelo.addAttribute(
            "cancelaciones",
            inscripcionRepository.listarCancelaciones(id)
        );

        modelo.addAttribute(
            "ocupados",
            inscripcionRepository.ocupados(id)
        );

        return "participantes";
    }
}
