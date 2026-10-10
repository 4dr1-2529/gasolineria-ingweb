package com.example.nexo.asistencia.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.asistencia.model.Asistencia;
import com.example.nexo.asistencia.service.AsistenciaService;
import com.example.nexo.auth.config.SecurityConfig;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;


/**
 * F35–F38 · Asistencia (P28 y P29).
 * La identidad del empleado sale de la sesión: el nombre de usuario autenticado
 * se busca en UsuarioService y su idEmpleado abre las marcaciones (RN05).
 * El navegador no envía ningún identificador de empleado.
 */
@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;

    public AsistenciaController(AsistenciaService asistenciaService, EmpleadoService empleadoService,
            UsuarioService usuarioService) {
        this.asistenciaService = asistenciaService;
        this.empleadoService = empleadoService;
        this.usuarioService = usuarioService;
    }

    /**
     * P28 · Mi asistencia: sólo el empleado de la sesión (no hay selector, RN05).
     */
    @GetMapping("/mi")
    public String mostrarMiAsistencia(Authentication autenticacion, Model model) {
        Integer idEmpleado = idEmpleadoDeLaSesion(autenticacion);
        Empleado empleado = asistenciaService.buscarEmpleado(idEmpleado);
        List<Asistencia> asistencias = asistenciaService.listaAsistenciasDeEmpleado(idEmpleado);
        LocalDate fechaHoy = LocalDate.now();
        Asistencia asistenciaHoy = null;
        for (Asistencia asistencia : asistencias) {
            if (fechaHoy.equals(asistencia.getFecha())) {
                asistenciaHoy = asistencia;
            }
        }
        model.addAttribute("empleadoActual", empleado);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("asistenciaHoy", asistenciaHoy);
        model.addAttribute("fechaHoy", fechaHoy);
        if (empleado == null) {
            // La cuenta existe pero no tiene empleado asociado
            model.addAttribute("mensaje",
                    "Tu cuenta no tiene un empleado asociado: pide al Administrador que registre tu ficha.");
            model.addAttribute("mensajeTipo", "error");
        }
        return "asistencia/mi"; // Retorna la vista correspondiente
    }

    @PostMapping("/entrada")
    public String registrarEntrada(Authentication autenticacion, RedirectAttributes redirect) {
        Integer idEmpleado = idEmpleadoDeLaSesion(autenticacion);
        redirect.addFlashAttribute("mensaje", asistenciaService.registrarEntrada(idEmpleado));
        return "redirect:/asistencia/mi";
    }

    @PostMapping("/salida")
    public String registrarSalida(Authentication autenticacion, RedirectAttributes redirect) {
        Integer idEmpleado = idEmpleadoDeLaSesion(autenticacion);
        redirect.addFlashAttribute("mensaje", asistenciaService.registrarSalida(idEmpleado));
        return "redirect:/asistencia/mi";
    }

    /**
     * P29 · Control de asistencia: consulta administrativa de todo el personal
     * (la ruta es sólo del Administrador, ver SecurityConfig).
     */
    @GetMapping("/control")
    public String mostrarControlAsistencia(
            @RequestParam(value = "idEmpleado", required = false) Integer idEmpleado, Model model) {
        List<Asistencia> asistencias = (idEmpleado == null)
                ? asistenciaService.listaAsistencias()
                : asistenciaService.listaAsistenciasDeEmpleado(idEmpleado);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleadosPorId", empleadosPorId());
        return "asistencia/control"; // Retorna la vista correspondiente
    }

    /**
     * Empleado dueño de la sesión: el usuario autenticado se traduce a su
     * cuenta y de ahí al idEmpleado. Nunca se toma un id de la petición.
     */
    private Integer idEmpleadoDeLaSesion(Authentication autenticacion) {
        if (autenticacion == null) {
            return null;
        }
        Usuario usuario = usuarioService.buscarUsuarioPorUsername(autenticacion.getName());
        return usuario == null ? null : usuario.getIdEmpleado();
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
