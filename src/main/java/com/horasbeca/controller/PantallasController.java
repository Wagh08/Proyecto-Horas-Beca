
package com.horasbeca.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.horasbeca.model.PerfilEstudiante;
import com.horasbeca.repository.PerfilRepository;
import com.horasbeca.repository.AcreditacionRepository;
import com.horasbeca.service.MetaAnualService;
import com.horasbeca.service.ResumenHorasService;

@Controller
public class PantallasController {

    private static final Set<String> PAGINAS = Set.of(
        "inicio-estudiante",
        "perfil-estudiante",
        "inicio-organizador",
        "crear-actividad",
        "solicitudes",
        "perfil-organizador"
    );

    private final PerfilRepository perfilRepository;
    private final MetaAnualService metaAnualService;
    private final AcreditacionRepository acreditacionRepository;
    private final ResumenHorasService resumenHorasService;

    public PantallasController(
            PerfilRepository perfilRepository,
            MetaAnualService metaAnualService,
            AcreditacionRepository acreditacionRepository,
            ResumenHorasService resumenHorasService) {

        this.perfilRepository = perfilRepository;
        this.metaAnualService = metaAnualService;
        this.acreditacionRepository = acreditacionRepository;
        this.resumenHorasService = resumenHorasService;
    }

    
    @GetMapping("/")
    public String inicio(Authentication autenticacion) {

        boolean esOrganizador = autenticacion.getAuthorities()
            .stream()
            .anyMatch(autoridad ->
                autoridad.getAuthority().equals("ROLE_ORGANIZADOR")
            );

        return esOrganizador
            ? "redirect:/inicio-organizador.html"
            : "redirect:/inicio-estudiante.html";
    }

    
    @GetMapping("/{pagina}.html")
    public String mostrarPagina(
            @PathVariable("pagina") String pagina,
            Authentication autenticacion,
            Model modelo) {

        if (!PAGINAS.contains(pagina)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND
            );
        }

        
        if (pagina.equals("inicio-estudiante")) {
            modelo.addAttribute(
                "resumen",
                resumenHorasService.calcular(autenticacion.getName())
            );
        }

        
        if (pagina.equals("perfil-estudiante")) {

            PerfilEstudiante perfil = perfilRepository
                .buscarEstudiantePorCorreo(autenticacion.getName())
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Perfil de estudiante no encontrado"
                    )
                );

            modelo.addAttribute("perfil", perfil);

            modelo.addAttribute(
                "metaAnual",
                metaAnualService.calcularHorasRequeridas(
                    perfil.porcentajeBeca()
                )
            );
        }

        
        if (pagina.equals("perfil-organizador")) {

            modelo.addAttribute(
                "perfil",
                perfilRepository
                    .buscarOrganizadorPorCorreo(
                        autenticacion.getName()
                    )
                    .orElseThrow(() ->
                        new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Perfil de organizador no encontrado"
                        )
                    )
            );
        }

        return pagina;
    }

    
    @GetMapping("/progreso.html")
    public String progreso(
            @RequestParam(name = "anio", required = false) Integer anio,
            Authentication autenticacion,
            Model modelo) {

        String correo = autenticacion.getName();

        
        ResumenHorasService.Resumen resumen =
            resumenHorasService.calcular(correo);

       
        List<Integer> anios =
            acreditacionRepository.listarAnios(correo, resumen.anio());

       
        int anioSeleccionado =
            anio == null ? resumen.anio() : anio;

       
        if (!anios.contains(anioSeleccionado)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Año del historial no disponible"
            );
        }

        
        modelo.addAttribute("resumen", resumen);
        modelo.addAttribute("aniosHistorial", anios);
        modelo.addAttribute("anioHistorial", anioSeleccionado);

        modelo.addAttribute(
            "historial",
            acreditacionRepository.listarAnuales(
                correo,
                anioSeleccionado
            )
        );

        return "progreso";
    }

    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
