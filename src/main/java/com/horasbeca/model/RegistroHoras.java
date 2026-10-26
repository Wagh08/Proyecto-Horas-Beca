package com.horasbeca.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RegistroHoras(String actividad, String organizador, String acreditadoEn, int horas) {

    public String fechaTexto() {
        return LocalDateTime.parse(acreditadoEn).format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm"));
    }

    public String fechaIso() {
        return LocalDateTime.parse(acreditadoEn).toLocalDate().toString();
    }
}