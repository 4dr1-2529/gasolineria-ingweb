package com.example.nexo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nexo.model.Categoria;
import com.example.nexo.service.CategoriaService;

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
        model.addAttribute("categoria", new Categoria());
        return "categoria/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearCategoria(@ModelAttribute("categoria") Categoria categoria) {
        categoriaService.crearCategoria(categoria);
        return "redirect:/categorias/list";
    }

}
