package com.example.nexo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.Categoria;
import com.example.nexo.service.CategoriaService;

/**
 * F04–F08 · Catálogo de familias (P05 lista, P22 formulario).
 * Cualquier validación fallida devuelve al formulario con el aviso del
 * servidor y conserva lo digitado; el éxito redirige al listado.
 * RN02: no hay ninguna ruta de eliminación, sólo cambio de estado (F08).
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/list")
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.listaCategorias();
        model.addAttribute("categorias", categorias);
        return "categoria/lista"; // Retorna la vista correspondiente
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", new Categoria());
        }
        return "categoria/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearCategoria(@ModelAttribute("categoria") Categoria categoria,
            RedirectAttributes redirect) {
        String error = categoriaService.crearCategoria(categoria);
        if (error != null) {
            // Se conserva lo digitado y se muestra el motivo del servidor
            redirect.addFlashAttribute("categoria", categoria);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/categorias/crear";
        }
        redirect.addFlashAttribute("mensaje", "La categoría se registró correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/categorias/list";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Integer id, Model model,
            RedirectAttributes redirect) {
        Categoria categoria = categoriaService.buscarCategoriaPorId(id);
        if (categoria == null) {
            redirect.addFlashAttribute("mensaje", "No se encontró la categoría solicitada.");
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/categorias/list";
        }
        model.addAttribute("categoria", categoria);
        return "categoria/editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/editar")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria,
            RedirectAttributes redirect) {
        String error = categoriaService.editarCategoria(categoria);
        if (error != null) {
            redirect.addFlashAttribute("categoria", categoria);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/categorias/editar?id=" + categoria.getId();
        }
        redirect.addFlashAttribute("mensaje", "La categoría se editó correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/categorias/list";
    }

    /**
     * F08 · RN02: cambiar estado sin eliminar. El id viaja oculto en el
     * formulario del listado; el estado lo decide el botón pulsado.
     */
    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam("id") Integer id,
            @RequestParam("estado") String estado, RedirectAttributes redirect) {
        String error = categoriaService.cambiarEstadoCategoria(id, estado);
        if (error != null) {
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
        } else {
            redirect.addFlashAttribute("mensaje",
                    "La categoría quedó " + estado + " (el registro se conserva, RN02).");
            redirect.addFlashAttribute("mensajeTipo", "ok");
        }
        return "redirect:/categorias/list";
    }

}
