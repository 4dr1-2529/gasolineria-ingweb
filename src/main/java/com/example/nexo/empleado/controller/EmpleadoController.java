package com.example.nexo.empleado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;


/**
 * F29–F31 · Personal de la estación (P18 lista, P25 formulario).
 * Cualquier validación fallida devuelve al formulario con el aviso del
 * servidor y conserva lo digitado; el éxito redirige al listado.
 * RN02: no hay ninguna ruta de eliminación, sólo cambio de estado (F31).
 */
@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/list")
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.listaEmpleados());
        return "empleado/lista"; // Retorna la vista correspondiente
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        if (!model.containsAttribute("empleado")) {
            model.addAttribute("empleado", new Empleado());
        }
        return "empleado/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearEmpleado(@ModelAttribute("empleado") Empleado empleado,
            RedirectAttributes redirect) {
        String error = empleadoService.crearEmpleado(empleado);
        if (error != null) {
            // Se conserva lo digitado y se muestra el motivo del servidor
            redirect.addFlashAttribute("empleado", empleado);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/empleados/crear";
        }
        redirect.addFlashAttribute("mensaje", "El empleado se registró correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/empleados/list";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Integer id, Model model,
            RedirectAttributes redirect) {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if (empleado == null) {
            // Id inexistente: se regresa a la lista sin mostrar un error
            redirect.addFlashAttribute("mensaje", "No se encontró el empleado solicitado.");
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/empleados/list";
        }
        model.addAttribute("empleado", empleado);
        return "empleado/editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/editar")
    public String editarEmpleado(@ModelAttribute("empleado") Empleado empleado,
            RedirectAttributes redirect) {
        String error = empleadoService.editarEmpleado(empleado);
        if (error != null) {
            redirect.addFlashAttribute("empleado", empleado);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/empleados/editar?id=" + empleado.getId();
        }
        redirect.addFlashAttribute("mensaje", "El empleado se editó correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/empleados/list";
    }

    /**
     * F31 · RN02: cambiar estado sin eliminar. El id viaja oculto en el
     * formulario del listado; el estado lo decide el botón pulsado.
     */
    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam("id") Integer id,
            @RequestParam("estado") String estado, RedirectAttributes redirect) {
        String error = empleadoService.cambiarEstadoEmpleado(id, estado);
        if (error != null) {
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
        } else {
            redirect.addFlashAttribute("mensaje",
                    "El empleado quedó " + estado + " (el registro se conserva, RN02).");
            redirect.addFlashAttribute("mensajeTipo", "ok");
        }
        return "redirect:/empleados/list";
    }

}
