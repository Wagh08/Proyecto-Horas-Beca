package com.horasbeca.service;

import com.horasbeca.model.Actividad;
import com.horasbeca.repository.ActividadRepository;
import com.horasbeca.repository.InscripcionRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class InicioService {

    private static final ZoneId ZONA = ZoneId.of("America/Guatemala");
    private static final Comparator<Actividad> ORDEN_FECHA = Comparator.comparing(Actividad::fecha).thenComparing(Actividad::horaInicio).thenComparingLong(Actividad::id);

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;

    public InicioService(ActividadRepository actividadRepository, InscripcionRepository inscripcionRepository) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    public record InicioEstudiante(List<Actividad> inscritas, List<Actividad> oportunidades, Map<Long, Integer> cupos) {
    }

    public record InicioOrganizador(List<Actividad> proximas, List<Actividad> pendientes, int totalProximas, int totalPendientes, Map<Long, Integer> ocupados, Map<Long, Integer> llegadasPendientes) {
    }

    public InicioEstudiante estudiante(String correo) {
        LocalDateTime ahora = LocalDateTime.now(ZONA);

        List<Actividad> inscritas = actividadRepository.listarInscritas(correo).stream()
            .filter(actividad -> LocalDateTime.of(actividad.fecha(), actividad.horaFin()).isAfter(ahora))
            .sorted(ORDEN_FECHA)
            .limit(2)
            .toList();

        Set<Long> idsInscritas = new HashSet<>(inscripcionRepository.listarIdsActivos(correo));
        Map<Long, Integer> cupos = new HashMap<>();

        List<Actividad> oportunidades = actividadRepository.listarDisponibles().stream()
            .filter(actividad -> !idsInscritas.contains(actividad.id()))
            .filter(actividad -> {
                int disponibles = Math.max(0, actividad.cupoMaximo() - inscripcionRepository.ocupados(actividad.id()));
                if (disponibles > 0) {
                    cupos.put(actividad.id(), disponibles);
                }
                return disponibles > 0;
            })
            .limit(2)
            .toList();

        return new InicioEstudiante(inscritas, oportunidades, cupos);
    }

    public InicioOrganizador organizador(String correo) {
        LocalDateTime ahora = LocalDateTime.now(ZONA);
        List<Actividad> propias = actividadRepository.listarPropias(correo);

        List<Actividad> todasLasProximas = propias.stream()
            .filter(actividad -> LocalDateTime.of(actividad.fecha(), actividad.horaFin()).isAfter(ahora))
            .sorted(ORDEN_FECHA)
            .toList();

        Map<Long, Integer> llegadasPendientes = new HashMap<>();

        for (Actividad actividad : propias) {
            int cantidad = inscripcionRepository.contarLlegadasPendientes(actividad.id());
            if (cantidad > 0) {
                llegadasPendientes.put(actividad.id(), cantidad);
            }
        }

        List<Actividad> pendientes = propias.stream()
            .filter(actividad -> llegadasPendientes.containsKey(actividad.id()))
            .sorted(ORDEN_FECHA)
            .limit(2)
            .toList();

        List<Actividad> proximas = todasLasProximas.stream().limit(2).toList();
        Map<Long, Integer> ocupados = new HashMap<>();

        for (Actividad actividad : proximas) {
            ocupados.put(actividad.id(), inscripcionRepository.ocupados(actividad.id()));
        }

        int totalPendientes = llegadasPendientes.values().stream().mapToInt(Integer::intValue).sum();

        return new InicioOrganizador(proximas, pendientes, todasLasProximas.size(), totalPendientes, ocupados, llegadasPendientes);
    }
}