package com.example.nexo.shared.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.shared.validation.ReglaNegocioException;

@ControllerAdvice
public class ErroresOperacion {
    @ExceptionHandler({ReglaNegocioException.class,DataIntegrityViolationException.class,
            PessimisticLockingFailureException.class})
    public String rechazar(RuntimeException error, HttpServletRequest request, RedirectAttributes flash) {
        flash.addFlashAttribute("mensaje", error instanceof ReglaNegocioException ? error.getMessage()
                : "La operación no se guardó: existe un duplicado, una relación inválida o una operación concurrente. Revisa el listado antes de reintentar.");
        flash.addFlashAttribute("mensajeTipo", "error");
        String ruta=request.getRequestURI().substring(request.getContextPath().length());
        if (ruta.startsWith("/asistencia/")) ruta="/asistencia/mi";
        if (ruta.startsWith("/finanzas/caja/")) ruta="/finanzas/list";
        if (ruta.endsWith("/estado")) ruta=ruta.substring(0,ruta.length()-7)+"/list";
        if (ruta.endsWith("/editar")) {
            String id=request.getParameter("id");
            if(id!=null && id.matches("[A-Za-z0-9]+")) ruta+="?id="+id;
        }
        return "redirect:"+ruta;
    }
}
