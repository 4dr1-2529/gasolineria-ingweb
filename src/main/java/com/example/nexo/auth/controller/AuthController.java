package com.example.nexo.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.nexo.auth.config.SecurityConfig;
import com.example.nexo.usuario.model.Usuario;

/**
 * F01 · Inicio de sesión y F02 · cierre de sesión.
 * El POST /login y el POST /logout los procesa Spring Security
 * (ver SecurityConfig); aquí sólo se sirven las vistas y sus avisos.
 */
@Controller
public class AuthController {

    /** Pantalla de acceso (P02 de la V2) con los avisos de error o de cierre. */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {
        if ("inactivo".equals(error)) {
            model.addAttribute("error", "La cuenta está inactiva: no puede iniciar sesión.");
        } else if ("credenciales".equals(error)) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        } else if (error != null) {
            model.addAttribute("error", "No se pudo iniciar sesión: verifique los datos.");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada correctamente.");
        }
        return "auth/login";
    }

    /**
     * Aviso claro cuando un rol intenta entrar a una sección que no le corresponde.
     * Acepta cualquier método: un POST denegado se reenvía a esta ruta
     * conservando el método y la vista sólo tiene que mostrarse.
     */
    @RequestMapping("/sin-permisos")
    public String sinPermisos() {
        return "error/permisos";
    }
}
