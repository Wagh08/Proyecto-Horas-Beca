
package com.horasbeca.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            ActividadRepository actividadRepository) {

        this.inscripcionRepository = inscripcionRepository;
        this.actividadRepository = actividadRepository;
    }

    // RF-06: INSCRIBIR ESTUDIANTE EN UNA ACTIVIDAD
    public void inscribir(
            long actividadId,
            String correoEstudiante) {

        Actividad actividad = actividadRepository
            .buscarPorId(actividadId)
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

    // RF-06: CANCELAR INSCRIPCION Y LIBERAR CUPO
    @Transactional
    public void cancelar(
            long actividadId,
            String correoEstudiante,
            String motivoTexto) {

        Actividad actividad = actividadRepository
            .buscarPorId(actividadId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "La actividad no existe."
                )
            );

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        LocalDateTime inicio = LocalDateTime.of(
            actividad.fecha(),
            actividad.horaInicio()
        );

        // NO PERMITIR CANCELAR DESPUES DEL INICIO
        if (!ahora.isBefore(inicio)) {
            throw new IllegalArgumentException(
                "La actividad ya comenzó."
            );
        }

        // VERIFICAR SI FALTA UNA HORA O MENOS
        boolean tardia =
            !ahora.isBefore(inicio.minusHours(1));

        String motivo = motivoTexto == null
            ? ""
            : motivoTexto.trim();

        // VALIDAR LONGITUD DE LA JUSTIFICACION
        if (motivo.length() > 500) {
            throw new IllegalArgumentException(
                "La justificación no puede superar 500 caracteres."
            );
        }

        // EXIGIR JUSTIFICACION SI LA CANCELACION ES TARDIA
        if (tardia && motivo.isBlank()) {
            throw new IllegalArgumentException(
                "Debes justificar una cancelación hecha " +
                "una hora o menos antes del inicio."
            );
        }

        String motivoGuardado =
            motivo.isBlank() ? null : motivo;

        // CAMBIAR ESTADO A CANCELADA
        if (inscripcionRepository.cancelar(
                actividadId,
                correoEstudiante,
                ahora,
                motivoGuardado,
                tardia
        ) != 1) {

            throw new IllegalArgumentException(
                "No tienes una inscripción activa que puedas cancelar."
            );
        }

        // GUARDAR HISTORIAL DE CANCELACION
        if (inscripcionRepository.guardarCancelacion(
                actividadId,
                correoEstudiante,
                ahora,
                motivoGuardado,
                tardia
        ) != 1) {

            throw new IllegalStateException(
                "No se pudo guardar el historial de cancelación."
            );
        }
    }

    // RF-07.01: GUARDAR AVISO DE LLEGADA
    public void avisarLlegada(
            long actividadId,
            String correoEstudiante) {

        Actividad actividad = actividadRepository
            .buscarPorId(actividadId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "La actividad no existe."
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

        // SOLO PERMITIR AVISAR DURANTE LA ACTIVIDAD
        if (ahora.isBefore(inicio) || !ahora.isBefore(fin)) {

            throw new IllegalArgumentException(
                "Solo puedes avisar tu llegada " +
                "durante el horario de la actividad."
            );
        }

        if (inscripcionRepository.avisarLlegada(
                actividadId,
                correoEstudiante,
                ahora
        ) != 1) {

            throw new IllegalArgumentException(
                "No tienes una inscripción pendiente " +
                "de avisar llegada."
            );
        }
    }

    // RF-07.03: VERIFICAR LLEGADA DEL ESTUDIANTE
    public void verificarLlegada(
            long inscripcionId,
            long actividadId,
            String correoOrganizador) {

        // COMPROBAR QUE LA ACTIVIDAD PERTENECE AL ORGANIZADOR
        Actividad actividad = actividadRepository
            .buscarPropiaPorId(
                actividadId,
                correoOrganizador
            )
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "No organizas esta actividad."
                )
            );

        LocalDateTime ahora = LocalDateTime.now(ZONA);

        LocalDateTime inicio = LocalDateTime.of(
            actividad.fecha(),
            actividad.horaInicio()
        );

        // NO VERIFICAR ANTES DEL INICIO
        if (ahora.isBefore(inicio)) {

            throw new IllegalArgumentException(
                "La actividad aún no comienza."
            );
        }

        // ACTUALIZAR SOLO AVISOS PENDIENTES
        if (inscripcionRepository.verificarLlegada(
                inscripcionId,
                actividadId,
                correoOrganizador,
                ahora
        ) != 1) {

            throw new IllegalArgumentException(
                "No existe un aviso de llegada pendiente " +
                "para este estudiante."
            );
        }
    }
}
