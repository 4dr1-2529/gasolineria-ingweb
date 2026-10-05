package com.example.nexo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nexo.model.Categoria;
import com.example.nexo.model.Producto;
import com.example.nexo.service.CategoriaService;
import com.example.nexo.service.ProductoService;

@Controller
@RequestMapping("/combustibles")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/list")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listaProductos();
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.listaCategorias());
        model.addAttribute("nombresCategorias", nombresCategorias());
        return "producto/lista"; // Retorna la vista correspondiente
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listaCategorias());
        return "producto/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearProducto(@ModelAttribute("producto") Producto producto) {
        productoService.crearProducto(producto);
        return "redirect:/combustibles/list";
    }

    /**
     * Sólo para mostrar el nombre de la categoría en la lista
     * (el modelo conserva la llave foránea id_categoria).
     */
    private Map<Integer, String> nombresCategorias() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Categoria categoria : categoriaService.listaCategorias()) {
            nombres.put(categoria.getId(), categoria.getNombre());
        }
        return nombres;
    }

}
