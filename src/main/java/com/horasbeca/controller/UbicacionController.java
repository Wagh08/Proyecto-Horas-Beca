package com.horasbeca.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.horasbeca.service.UbicacionService;

@RestController
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/api/ubicaciones")
    public List<String> sugerir(@RequestParam(name = "q", defaultValue = "") String texto) {
        return ubicacionService.sugerir(texto);
    }
}