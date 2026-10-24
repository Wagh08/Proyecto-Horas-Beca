package com.horasbeca.controller;

import com.horasbeca.repository.PerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ContactoController {

    private final PerfilRepository perfilRepository;

    public ContactoController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @PostMapping("/perfil-estudiante/contacto")
    public String actualizarContactoEstudiante(@RequestParam("telefono") String telefono, Authentication autenticacion) {
        String nuevoTelefono = telefono.trim();

        if (nuevoTelefono.length() > 25) {
            return "redirect:/perfil-estudiante.html?contactoError";
        }

        if (!perfilRepository.actualizarTelefonoEstudiante(autenticacion.getName(), nuevoTelefono.isEmpty() ? null : nuevoTelefono)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de estudiante no encontrado");
        }

        return "redirect:/perfil-estudiante.html?contactoGuardado";
    }

    @PostMapping("/perfil-organizador/contacto")
    public String actualizarContactoOrganizador(@RequestParam("telefono") String telefono, Authentication autenticacion) {
        String nuevoTelefono = telefono.trim();

        if (nuevoTelefono.length() > 25) {
            return "redirect:/perfil-organizador.html?contactoError";
        }

        if (!perfilRepository.actualizarTelefonoOrganizador(autenticacion.getName(), nuevoTelefono.isEmpty() ? null : nuevoTelefono)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de organizador no encontrado");
        }

        return "redirect:/perfil-organizador.html?contactoGuardado";
    }
}