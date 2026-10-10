package com.example.nexo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.Empleado;
import com.example.nexo.model.Usuario;
import com.example.nexo.service.EmpleadoService;
import com.example.nexo.service.UsuarioService;

/**
 * F32 · Gestión de usuarios (P19, P26): el listado consulta y el formulario
 * registra, edita y cambia el estado. La sección es sólo del Administrador
 * (SecurityConfig) y el servidor valida duplicados, formato, rol y estado:
 * un formulario manipulado nunca cambia el rol por la puerta trasera.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public UsuarioController(UsuarioService usuarioService, EmpleadoService empleadoService) {
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/list")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listaUsuarios());
        model.addAttribute("nombresEmpleados", nombresEmpleados());
        return "usuario/lista"; // Retorna la vista correspondiente
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        // (salvo que venga uno conservado de un intento rechazado)
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        model.addAttribute("empleados", empleadoService.listaEmpleados());
        model.addAttribute("nombresEmpleados", nombresEmpleados());
        return "usuario/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirect) {
        String error = usuarioService.crearUsuario(usuario);
        if (error != null) {
            // Cuenta rechazada: se vuelve al formulario conservando lo digitado
            redirect.addFlashAttribute("usuario", usuario);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/usuarios/crear";
        }
        redirect.addFlashAttribute("mensaje",
                "Usuario " + usuario.getUsername() + " registrado correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/usuarios/list";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        if (usuario == null) {
            // Id inexistente: se regresa a la lista sin mostrar un error
            return "redirect:/usuarios/list";
        }
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", usuario);
        }
        return "usuario/editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/editar")
    public String editarUsuario(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirect) {
        String error = usuarioService.editarUsuario(usuario);
        if (error != null) {
            redirect.addFlashAttribute("usuario", usuario);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/usuarios/editar?id=" + usuario.getId();
        }
        redirect.addFlashAttribute("mensaje",
                "Usuario " + usuario.getUsername() + " actualizado correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/usuarios/list";
    }

    /**
     * Los empleados se toman de EmpleadoService (no hay una segunda lista):
     * sirven para el selector del formulario y para mostrar el nombre en la lista.
     */
    private Map<Integer, String> nombresEmpleados() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Empleado empleado : empleadoService.listaEmpleados()) {
            nombres.put(empleado.getId(), empleado.getNombres() + " " + empleado.getApellidos());
        }
        return nombres;
    }

}
