package com.horasbeca.controller;

import com.horasbeca.model.ResultadoQr;
import com.horasbeca.service.AcreditacionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ValidacionQrController {

    private final AcreditacionService acreditacionService;

    public ValidacionQrController(AcreditacionService acreditacionService) {
        this.acreditacionService = acreditacionService;
    }

    @PostMapping("/actividades/{id}/validar-qr")
    public String validar(@PathVariable("id") long id, @RequestParam("codigo") String codigo, Authentication autenticacion, RedirectAttributes mensajes) {
        try {
            ResultadoQr resultado = acreditacionService.acreditar(id, autenticacion.getName(), codigo);
            mensajes.addFlashAttribute("resultado", resultado);
        } catch (IllegalArgumentException excepcion) {
            mensajes.addFlashAttribute("errorQr", excepcion.getMessage());
        }

        return "redirect:/escanear-qr.html?id=" + id;
    }
}