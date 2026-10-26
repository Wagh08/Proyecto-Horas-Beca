package com.horasbeca.model;

public record Participante(
    long id,
    String nombre,
    String carne,
    String estado,
    String avisoLlegadaEn,
    String verificadoEn
) {
}
