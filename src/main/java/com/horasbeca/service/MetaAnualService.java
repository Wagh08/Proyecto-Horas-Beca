package com.horasbeca.service;

import org.springframework.stereotype.Service;

@Service
public class MetaAnualService {

    public int calcularHorasRequeridas(int porcentajeBeca) {
        if (porcentajeBeca < 1 || porcentajeBeca > 100) {
            throw new IllegalArgumentException("El porcentaje de beca debe estar entre 1 y 100");
        }

        if (porcentajeBeca <= 15) {
            return 30;
        }
        if (porcentajeBeca <= 25) {
            return 50;
        }
        if (porcentajeBeca <= 50) {
            return 70;
        }
        if (porcentajeBeca <= 75) {
            return 80;
        }
        return 100;
    }
}