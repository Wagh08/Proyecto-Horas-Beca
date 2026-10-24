package com.horasbeca.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MetaAnualServiceTest {

    private final MetaAnualService metaAnualService = new MetaAnualService();

    @Test
    void calculaLaMetaEnLosLimitesDeCadaRango() {
        assertEquals(30, metaAnualService.calcularHorasRequeridas(1));
        assertEquals(30, metaAnualService.calcularHorasRequeridas(15));
        assertEquals(50, metaAnualService.calcularHorasRequeridas(16));
        assertEquals(50, metaAnualService.calcularHorasRequeridas(25));
        assertEquals(70, metaAnualService.calcularHorasRequeridas(26));
        assertEquals(70, metaAnualService.calcularHorasRequeridas(50));
        assertEquals(80, metaAnualService.calcularHorasRequeridas(51));
        assertEquals(80, metaAnualService.calcularHorasRequeridas(75));
        assertEquals(100, metaAnualService.calcularHorasRequeridas(76));
        assertEquals(100, metaAnualService.calcularHorasRequeridas(100));
    }

    @Test
    void rechazaPorcentajesFueraDeLosRangos() {
        assertThrows(IllegalArgumentException.class, () -> metaAnualService.calcularHorasRequeridas(0));
        assertThrows(IllegalArgumentException.class, () -> metaAnualService.calcularHorasRequeridas(-1));
        assertThrows(IllegalArgumentException.class, () -> metaAnualService.calcularHorasRequeridas(101));
    }
}