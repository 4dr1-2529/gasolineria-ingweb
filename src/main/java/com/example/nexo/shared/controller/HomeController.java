package com.example.nexo.shared.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Entrada al dashboard persistente según el rol de la sesión. */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Authentication autenticacion) {return "redirect:/dashboard";}
}
