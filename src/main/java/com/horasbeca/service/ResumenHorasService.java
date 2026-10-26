package com.horasbeca.service;

import com.horasbeca.model.PerfilEstudiante;
import com.horasbeca.repository.AcreditacionRepository;
import com.horasbeca.repository.PerfilRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResumenHorasService {

    private static final ZoneId ZONA = ZoneId.of("America/Guatemala");

    private final PerfilRepository perfilRepository;
    private final MetaAnualService metaAnualService;
    private final AcreditacionRepository acreditacionRepository;

    public ResumenHorasService(PerfilRepository perfilRepository, MetaAnualService metaAnualService, AcreditacionRepository acreditacionRepository) {
        this.perfilRepository = perfilRepository;
        this.metaAnualService = metaAnualService;
        this.acreditacionRepository = acreditacionRepository;
    }

    public record Resumen(int anio, String nombre, int meta, int acreditadas, int pendientes, int porcentaje, int anchoBarra) {
    }

    public Resumen calcular(String correoEstudiante) {
        PerfilEstudiante perfil = perfilRepository.buscarEstudiantePorCorreo(correoEstudiante)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de estudiante no encontrado"));

        int anio = LocalDate.now(ZONA).getYear();
        int meta = metaAnualService.calcularHorasRequeridas(perfil.porcentajeBeca());
        int acreditadas = acreditacionRepository.totalAnual(correoEstudiante, anio);
        int pendientes = Math.max(0, meta - acreditadas);
        int porcentaje = (int) Math.round(acreditadas * 100.0 / meta);

        return new Resumen(anio, perfil.nombre(), meta, acreditadas, pendientes, porcentaje, Math.min(100, porcentaje));
    }
}
