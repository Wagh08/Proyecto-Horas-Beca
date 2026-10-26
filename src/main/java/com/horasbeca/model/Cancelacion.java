package com.horasbeca.model;

public record Cancelacion(
    String nombre,
    String carne,
    String canceladoEn,
    String motivo,
    boolean tardia
) {
}

