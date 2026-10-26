package com.horasbeca.model;

public record Inscripcion(
    long id,
    long actividadId,
    String estado,
    String inscritoEn,
    String canceladoEn,
    String motivoCancelacion,
    boolean canceladaTarde,
    String avisoLlegadaEn,
    String verificadoEn
) {
}