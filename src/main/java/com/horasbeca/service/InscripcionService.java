package com.horasbeca.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.horasbeca.model.Actividad;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;

@Service
public class InscripcionService {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final InscripcionRepository inscripcionRepository;
    private final ActividadRepository actividadRepository;

    public InscripcionService(
        InscripcionRepository inscripcionRepository,
        ActividadRepository actividadRepository
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.actividadRepository = actividadRepository;
    }

    public void inscribir(
        long actividadId,
        String correoEstudiante
    ) {
        Actividad actividad =
            actividadRepository.buscarPorId(actividadId)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "La actividad no existe."
                    )
                );

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        if (!ahora.isBefore(
            LocalDateTime.of(
                actividad.fecha(),
                actividad.horaInicio()
            )
        )) {
            throw new IllegalArgumentException(
                "La inscripción cerró al comenzar la actividad."
            );
        }

        if (inscripcionRepository.inscribir(
            actividadId,
            correoEstudiante,
            ahora
        ) != 1) {
            throw new IllegalArgumentException(
                "No quedan cupos o ya tienes una inscripción activa."
            );
        }
    }
}
