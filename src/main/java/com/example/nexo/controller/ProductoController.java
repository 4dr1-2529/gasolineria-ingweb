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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.Categoria;
import com.example.nexo.model.Producto;
import com.example.nexo.service.CategoriaService;
import com.example.nexo.service.ProductoService;

/**
 * F09–F12 · Catálogo de combustibles (P06 lista, P07 formulario).
 * Cualquier validación fallida devuelve al formulario con el aviso del
 * servidor y conserva lo digitado; el éxito redirige al listado.
 * RN02: no hay ninguna ruta de eliminación, sólo cambio de estado (F12).
 */
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
        if (!model.containsAttribute("producto")) {
            model.addAttribute("producto", new Producto());
        }
        model.addAttribute("categorias", categoriasActivas());
        return "producto/crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/crear")
    public String crearProducto(@ModelAttribute("producto") Producto producto,
            RedirectAttributes redirect) {
        String error = productoService.crearProducto(producto);
        if (error != null) {
            // Se conserva lo digitado y se muestra el motivo del servidor
            redirect.addFlashAttribute("producto", producto);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/combustibles/crear";
        }
        redirect.addFlashAttribute("mensaje", "El combustible se registró correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/combustibles/list";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") String id, Model model,
            RedirectAttributes redirect) {
        Producto producto = productoService.buscarProductoPorId(id);
        if (producto == null) {
            redirect.addFlashAttribute("mensaje", "No se encontró el combustible solicitado.");
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/combustibles/list";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listaCategorias());
        return "producto/editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/editar")
    public String editarProducto(@ModelAttribute("producto") Producto producto,
            RedirectAttributes redirect) {
        String error = productoService.editarProducto(producto);
        if (error != null) {
            redirect.addFlashAttribute("producto", producto);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/combustibles/editar?id=" + producto.getId();
        }
        redirect.addFlashAttribute("mensaje", "El combustible se editó correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/combustibles/list";
    }

    /**
     * F12 · RN02: cambiar estado sin eliminar. El código viaja oculto en el
     * formulario del listado; el estado lo decide el botón pulsado.
     */
    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam("id") String id,
            @RequestParam("estado") String estado, RedirectAttributes redirect) {
        String error = productoService.cambiarEstadoProducto(id, estado);
        if (error != null) {
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
        } else {
            redirect.addFlashAttribute("mensaje",
                    "El combustible quedó " + estado + " (el registro se conserva, RN02).");
            redirect.addFlashAttribute("mensajeTipo", "ok");
        }
        return "redirect:/combustibles/list";
    }

    /**
     * El alta sólo admite familias Activas: una categoría Inactiva no se usa
     * en operaciones nuevas (RN02). La edición muestra todas para poder
     * conservar la actual.
     */
    private List<Categoria> categoriasActivas() {
        List<Categoria> activas = new java.util.ArrayList<>();
        for (Categoria categoria : categoriaService.listaCategorias()) {
            if ("Activo".equals(categoria.getEstado())) {
                activas.add(categoria);
            }
        }
        return activas;
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
