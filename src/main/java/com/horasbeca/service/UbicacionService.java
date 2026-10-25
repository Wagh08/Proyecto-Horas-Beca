package com.horasbeca.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.horasbeca.repository.UbicacionRepository;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    public List<String> sugerir(String texto) {
        String busqueda = normalizar(texto);

        if (busqueda.length() < 2) {
            return List.of();
        }

        return ubicacionRepository.listarUbicaciones().stream().filter(ubicacion -> coincide(ubicacion, busqueda)).limit(8).toList();
    }

    public boolean coincide(String ubicacion, String texto) {
        String busqueda = normalizar(texto);
        return busqueda.isEmpty() || normalizar(ubicacion).startsWith(busqueda);
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return sinTildes.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}