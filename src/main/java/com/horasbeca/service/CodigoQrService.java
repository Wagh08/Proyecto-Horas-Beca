
package com.horasbeca.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.horasbeca.model.Actividad;
import com.horasbeca.model.Inscripcion;

import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.CodigoQrRepository;
import com.horasbeca.repository.InscripcionRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CodigoQrService {

    private static final ZoneId ZONA =
        ZoneId.of("America/Guatemala");

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CodigoQrRepository codigoQrRepository;

    public CodigoQrService(
            ActividadRepository actividadRepository,
            InscripcionRepository inscripcionRepository,
            CodigoQrRepository codigoQrRepository) {

        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.codigoQrRepository = codigoQrRepository;
    }

    // INFORMACION PARA MOSTRAR EL QR
    public record VistaQr(
        Actividad actividad,
        Inscripcion inscripcion,
        boolean finalizada,
        String token,
        String imagen
    ) {}

    // CONSULTAR EL QR DE UNA ACTIVIDAD
    public VistaQr consultar(
            long actividadId,
            String correoEstudiante) {

        Actividad actividad =
            actividadRepository.buscarPorId(actividadId)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actividad no encontrada"
                    )
                );

        Inscripcion inscripcion =
            inscripcionRepository.buscar(
                actividadId,
                correoEstudiante
            )
            .filter(registro ->
                !registro.estado().equals("CANCELADA")
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No tienes una inscripción activa"
                )
            );

        boolean finalizada =
            !LocalDateTime.now(ZONA).isBefore(
                LocalDateTime.of(
                    actividad.fecha(),
                    actividad.horaFin()
                )
            );

        String token =
            finalizada &&
            inscripcion.estado().equals("LLEGADA_VERIFICADA")
                ? codigoQrRepository
                    .buscarToken(inscripcion.id())
                    .orElse(null)
                : null;

        String imagen =
            token == null
                ? null
                : generarImagen(token);

        return new VistaQr(
            actividad,
            inscripcion,
            finalizada,
            token,
            imagen
        );
    }

    // GENERAR EL CODIGO QR
    public void generar(
            long actividadId,
            String correoEstudiante) {

        VistaQr vista =
            consultar(actividadId, correoEstudiante);

        if (!vista.finalizada() ||
            !vista.inscripcion().estado()
                .equals("LLEGADA_VERIFICADA")) {

            throw new IllegalArgumentException(
                "El QR requiere que termine la actividad " +
                "y se verifique tu llegada."
            );
        }

        if (vista.token() != null) {
            return;
        }

        codigoQrRepository.crearSiFalta(
            vista.inscripcion().id(),
            correoEstudiante,
            UUID.randomUUID().toString(),
            LocalDateTime.now(ZONA)
        );
    }

    // CONVERTIR EL TOKEN EN UNA IMAGEN QR
    private String generarImagen(String token) {

        try {

            BitMatrix matriz =
                new QRCodeWriter().encode(
                    token,
                    BarcodeFormat.QR_CODE,
                    280,
                    280
                );

            BufferedImage imagen =
                new BufferedImage(
                    matriz.getWidth(),
                    matriz.getHeight(),
                    BufferedImage.TYPE_INT_RGB
                );

            for (int y = 0; y < matriz.getHeight(); y++) {
                for (int x = 0; x < matriz.getWidth(); x++) {

                    imagen.setRGB(
                        x,
                        y,
                        matriz.get(x, y)
                            ? 0x000000
                            : 0xFFFFFF
                    );
                }
            }

            ByteArrayOutputStream salida =
                new ByteArrayOutputStream();

            ImageIO.write(imagen, "png", salida);

            return Base64.getEncoder()
                .encodeToString(salida.toByteArray());

        } catch (WriterException | IOException excepcion) {

            throw new IllegalStateException(
                "No se pudo generar la imagen del QR.",
                excepcion
            );
        }
    }
}
