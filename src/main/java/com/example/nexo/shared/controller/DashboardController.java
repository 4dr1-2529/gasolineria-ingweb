package com.example.nexo.shared.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.nexo.shared.service.DashboardService;
@Controller
public class DashboardController {
    private final DashboardService servicio;public DashboardController(DashboardService servicio){this.servicio=servicio;}
    @GetMapping("/dashboard")
    public String mostrar(Authentication auth,Model model){
        boolean admin=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        boolean vendedor=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_VENDEDOR"));
        model.addAllAttributes(servicio.datos());return "shared/dashboard";
    }
}
