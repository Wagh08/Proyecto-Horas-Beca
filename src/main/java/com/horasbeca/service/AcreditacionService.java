package com.horasbeca.service;

import com.horasbeca.model.ResultadoQr;
import com.horasbeca.repository.AcreditacionRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
public class AcreditacionService {

    private static final ZoneId ZONA = ZoneId.of("America/Guatemala");

    private final ValidacionQrService validacionQrService;
    private final AcreditacionRepository acreditacionRepository;

    public AcreditacionService(ValidacionQrService validacionQrService, AcreditacionRepository acreditacionRepository) {
        this.validacionQrService = validacionQrService;
        this.acreditacionRepository = acreditacionRepository;
    }

    public ResultadoQr acreditar(long actividadId, String correoOrganizador, String codigo) {
        ResultadoQr resultado = validacionQrService.validar(actividadId, correoOrganizador, codigo);

        if (acreditacionRepository.registrarSiValida(actividadId, correoOrganizador, codigo.trim(), LocalDateTime.now(ZONA)) != 1) {
            throw new IllegalArgumentException("No se acreditaron horas: este QR ya fue utilizado o dejó de ser válido.");
        }

        return resultado;
    }
}