package com.horasbeca.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record Actividad(long id, String nombre, String descripcion, String requisitos, String ubicacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int cupoMaximo, int horasAcreditables, String organizador) {

    public String fechaTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' uuuu", Locale.forLanguageTag("es-GT"));
        return fecha.format(formato);
    }
}