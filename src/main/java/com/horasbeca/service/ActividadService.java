package com.horasbeca.service;

import com.horasbeca.model.Actividad;
import com.horasbeca.repository.ActividadRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.List; 
import org.springframework.stereotype.Service;

@Service
public class ActividadService {

    private static final ZoneId ZONA = ZoneId.of("America/Guatemala");

    private final ActividadRepository actividadRepository;

    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public void publicar(Map<String, String> campos, String correoOrganizador) {
        String nombre = textoObligatorio(campos, "nombre", "nombre", 120);
        String descripcion = textoObligatorio(campos, "descripcion", "descripción", 2000);
        String requisitos = textoOpcional(campos.get("requisitos"), "requisitos", 1000);
        String ubicacion = textoObligatorio(campos, "ubicacion", "ubicación", 180);
        LocalDate fecha = leerFecha(campos.get("fecha"));
        LocalTime inicio = leerHora(campos.get("inicio"), "inicio");
        LocalTime fin = leerHora(campos.get("fin"), "finalización");
        int cupo = enteroPositivo(campos.get("cupo"), "cupo máximo");
        int horas = enteroPositivo(campos.get("horas"), "horas acreditables");

        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La hora de finalización debe ser posterior a la de inicio.");
        }

        if (!LocalDateTime.of(fecha, inicio).isAfter(LocalDateTime.now(ZONA))) {
            throw new IllegalArgumentException("La actividad debe comenzar en una fecha y hora futuras.");
        }

        Actividad actividad = new Actividad(0, nombre, descripcion, requisitos, ubicacion, fecha, inicio, fin, cupo, horas, "");

        if (actividadRepository.guardar(actividad, correoOrganizador) != 1) {
            throw new IllegalArgumentException("No se encontró tu perfil de organizador.");
        }
    }

    private String textoObligatorio(Map<String, String> campos, String clave, String etiqueta, int maximo) {
        String valor = campos.get(clave);

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Debes indicar " + etiqueta + ".");
        }

        valor = valor.trim();

        if (valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + etiqueta + " supera el límite de " + maximo + " caracteres.");
        }

        return valor;
    }

    private String textoOpcional(String valor, String etiqueta, int maximo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        valor = valor.trim();

        if (valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + etiqueta + " supera el límite de " + maximo + " caracteres.");
        }

        return valor;
    }

    private LocalDate leerFecha(String valor) {
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException | NullPointerException excepcion) {
            throw new IllegalArgumentException("Indica una fecha válida.");
        }
    }

    private LocalTime leerHora(String valor, String etiqueta) {
        try {
            return LocalTime.parse(valor);
        } catch (DateTimeParseException | NullPointerException excepcion) {
            throw new IllegalArgumentException("Indica una hora válida de " + etiqueta + ".");
        }
    }

    private int enteroPositivo(String valor, String etiqueta) {
        try {
            int numero = Integer.parseInt(valor);

            if (numero > 0) {
                return numero;
            }
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException("Indica un número entero positivo para " + etiqueta + ".");
        }

        throw new IllegalArgumentException("Indica un número entero positivo para " + etiqueta + ".");
    }

    public List<Actividad> filtrarDisponibles(String fechaTexto, String turno, String horasMinimasTexto) {
        LocalDate fecha = null;
        Integer horasMinimas = null;

        if (!fechaTexto.isBlank()) {
            try {
                fecha = LocalDate.parse(fechaTexto);
            } catch (DateTimeParseException excepcion) {
                throw new IllegalArgumentException("Indica una fecha válida para el filtro.");
            }
        }

        if (!turno.isEmpty() && !turno.equals("manana") && !turno.equals("tarde")) {
            throw new IllegalArgumentException("Selecciona un horario válido.");
        }

        if (!horasMinimasTexto.isBlank()) {
            horasMinimas = enteroPositivo(horasMinimasTexto, "mínimo de horas");
        }

        return actividadRepository.listarDisponibles(fecha, turno, horasMinimas);
    }
}