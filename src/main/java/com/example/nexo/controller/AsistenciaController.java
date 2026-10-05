package com.example.nexo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.Asistencia;
import com.example.nexo.model.Empleado;
import com.example.nexo.service.AsistenciaService;
import com.example.nexo.service.EmpleadoService;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final EmpleadoService empleadoService;

    public AsistenciaController(AsistenciaService asistenciaService, EmpleadoService empleadoService) {
        this.asistenciaService = asistenciaService;
        this.empleadoService = empleadoService;
    }

    /**
     * P28 · Mi asistencia: sólo el empleado actual (no hay selector, RN05).
     */
    @GetMapping("/mi")
    public String mostrarMiAsistencia(Model model) {
        List<Asistencia> asistencias = asistenciaService.listaAsistenciasEmpleadoActual();
        LocalDate fechaHoy = LocalDate.now();
        Asistencia asistenciaHoy = null;
        for (Asistencia asistencia : asistencias) {
            if (fechaHoy.equals(asistencia.getFecha())) {
                asistenciaHoy = asistencia;
            }
        }
        model.addAttribute("empleadoActual", asistenciaService.getEmpleadoActual());
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("asistenciaHoy", asistenciaHoy);
        model.addAttribute("fechaHoy", fechaHoy);
        return "asistencia/mi"; // Retorna la vista correspondiente
    }

    @PostMapping("/entrada")
    public String registrarEntrada(RedirectAttributes redirect) {
        redirect.addFlashAttribute("mensaje", asistenciaService.registrarEntrada());
        return "redirect:/asistencia/mi";
    }

    @PostMapping("/salida")
    public String registrarSalida(RedirectAttributes redirect) {
        redirect.addFlashAttribute("mensaje", asistenciaService.registrarSalida());
        return "redirect:/asistencia/mi";
    }

    /**
     * P29 · Control de asistencia: consulta administrativa de todo el personal.
     */
    @GetMapping("/control")
    public String mostrarControlAsistencia(
            @RequestParam(value = "idEmpleado", required = false) Integer idEmpleado, Model model) {
        List<Asistencia> asistencias = (idEmpleado == null)
                ? asistenciaService.listaAsistencias()
                : asistenciaService.listaAsistenciasPorEmpleado(idEmpleado);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleadosPorId", empleadosPorId());
        return "asistencia/control"; // Retorna la vista correspondiente
    }

    /**
     * Los empleados se toman de EmpleadoService (no hay una segunda lista):
     * sirven para el filtro de la consulta y para mostrar los datos de cada fila.
     */
    private Map<Integer, Empleado> empleadosPorId() {
        Map<Integer, Empleado> empleados = new HashMap<>();
        for (Empleado empleado : empleadoService.listaEmpleados()) {
            empleados.put(empleado.getId(), empleado);
        }
        return empleados;
    }

}
