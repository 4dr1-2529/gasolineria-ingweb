package com.example.nexo.shared.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Inicio de la aplicación: después del login cada rol entra a su
 * primera pantalla oficial (el tablero P03 sigue siendo sólo V1).
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Authentication autenticacion) {
        if (autenticacion != null
                && autenticacion.getAuthorities().stream()
                        .anyMatch(rol -> "ROLE_ADMIN".equals(rol.getAuthority()))) {
            return "redirect:/categorias/list"; // P05 · primera pantalla del Administrador
        }
        return "redirect:/ventas/list"; // P09 · el Operador ve el historial de ventas
    }
}
