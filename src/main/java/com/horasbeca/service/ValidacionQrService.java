
package com.horasbeca.service;

import com.horasbeca.model.ResultadoQr;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.CodigoQrRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ValidacionQrService {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final ActividadRepository actividadRepository;
    private final CodigoQrRepository codigoQrRepository;

    public ValidacionQrService(
            ActividadRepository actividadRepository,
            CodigoQrRepository codigoQrRepository) {

        this.actividadRepository = actividadRepository;
        this.codigoQrRepository = codigoQrRepository;
    }

    public ResultadoQr validar(
            long actividadId,
            String correoOrganizador,
            String codigoTexto) {

        actividadRepository.buscarPropiaPorId(
            actividadId,
            correoOrganizador
        ).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Actividad no encontrada"
            )
        );

        String codigo = codigoTexto == null
            ? ""
            : codigoTexto.trim();

        if (!codigo.matches(
                "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"
        )) {
            throw new IllegalArgumentException(
                "El código QR no tiene un formato válido."
            );
        }

        return codigoQrRepository.validar(
            actividadId,
            correoOrganizador,
            codigo,
            LocalDateTime.now(ZONA)
        ).orElseThrow(() ->
            new IllegalArgumentException(
                "QR rechazado: comprueba la actividad, " +
                "el horario, la llegada verificada " +
                "y si ya se acreditó."
            )
        );
    }
}
