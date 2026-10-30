package com.example.nexo.finanzas.controller;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.finanzas.service.CajaDiariaService;
import com.example.nexo.shared.validation.ReglaNegocioException;
@Controller @RequestMapping("/finanzas/caja")
public class CajaDiariaController {
    private final CajaDiariaService cajas;
    public CajaDiariaController(CajaDiariaService cajas){this.cajas=cajas;}
    @PostMapping("/abrir") public String abrir(@RequestParam String saldo,RedirectAttributes flash) {
        try {cajas.abrir(new BigDecimal(saldo));}catch(NumberFormatException e){throw new ReglaNegocioException("Introduce un saldo numérico válido.");}
        flash.addFlashAttribute("mensaje","Caja del día abierta.");return "redirect:/finanzas/list";
    }
    @PostMapping("/cerrar") public String cerrar(RedirectAttributes flash) {
        cajas.cerrar();flash.addFlashAttribute("mensaje","Caja cerrada con saldo calculado desde sus movimientos.");return "redirect:/finanzas/list";
    }
}
