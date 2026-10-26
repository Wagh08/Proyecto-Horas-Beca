
package com.horasbeca.controller;

import com.horasbeca.service.CodigoQrService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.horasbeca.repository.AcreditacionRepository;

@Controller
public class CodigoQrController {

    private final CodigoQrService codigoQrService;
    private final AcreditacionRepository acreditacionRepository;

    public CodigoQrController(CodigoQrService codigoQrService, AcreditacionRepository acreditacionRepository) {
        this.codigoQrService = codigoQrService;
        this.acreditacionRepository = acreditacionRepository;
    }

    // MOSTRAR QR DEL ESTUDIANTE
    @GetMapping("/qr-actividad.html")
    public String ver(
            @RequestParam("id") long id,
            Authentication autenticacion,
            Model modelo,
            HttpServletResponse respuesta) {

        respuesta.setHeader(
            "Cache-Control",
            "no-store"
        );

        CodigoQrService.VistaQr vista = codigoQrService.consultar(id, autenticacion.getName());
        modelo.addAttribute("vista", vista);
        modelo.addAttribute("acreditada", acreditacionRepository.existe(vista.inscripcion().id()));

        return "qr-actividad";
    }

    // GENERAR QR DE LA ACTIVIDAD
    @PostMapping("/actividades/{id}/generar-qr")
    public String generar(
            @PathVariable("id") long id,
            Authentication autenticacion) {

        try {

            codigoQrService.generar(
                id,
                autenticacion.getName()
            );

            return "redirect:/qr-actividad.html?id=" + id;

        } catch (IllegalArgumentException excepcion) {

            return "redirect:/qr-actividad.html?id="
                + id + "&errorQr";
        }
    }
}
