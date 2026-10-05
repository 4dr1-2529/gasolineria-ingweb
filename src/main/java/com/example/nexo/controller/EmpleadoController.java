package com.example.nexo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nexo.model.Empleado;
import com.example.nexo.service.EmpleadoService;

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
        model.addAttribute("empleado", new Empleado());
        return "empleado/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.crearEmpleado(empleado);
        return "redirect:/empleados/list";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Integer id, Model model) {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if (empleado == null) {
            // Id inexistente: se regresa a la lista sin mostrar un error
            return "redirect:/empleados/list";
        }
        model.addAttribute("empleado", empleado);
        return "empleado/editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/editar")
    public String editarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.editarEmpleado(empleado);
        return "redirect:/empleados/list";
    }

}
