package com.example.nexo.shared.controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
@ControllerAdvice
public class PerfilVista {
    private final Environment entorno;public PerfilVista(Environment entorno){this.entorno=entorno;}
    @ModelAttribute("demostracion") public boolean demostracion(){return entorno.acceptsProfiles(Profiles.of("demo"));}
}
