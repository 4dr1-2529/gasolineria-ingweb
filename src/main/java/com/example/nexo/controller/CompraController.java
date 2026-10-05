package com.example.nexo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.Compra;
import com.example.nexo.model.DetalleCompra;
import com.example.nexo.model.Empleado;
import com.example.nexo.model.Producto;
import com.example.nexo.model.Usuario;
import com.example.nexo.service.CompraService;
import com.example.nexo.service.EmpleadoService;
import com.example.nexo.service.ProductoService;
import com.example.nexo.service.UsuarioService;

/**
 * Compras: consulta del listado, registro con su detalle y vista de detalle.
 * El controller sólo prepara datos para las vistas y delega el registro en
 * CompraService (RN03 y RN04).
 */
@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public CompraController(CompraService compraService, ProductoService productoService,
            UsuarioService usuarioService, EmpleadoService empleadoService) {
        this.compraService = compraService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/list")
    public String listarCompras(Model model) {
        List<Compra> compras = compraService.listaCompras();
        List<DetalleCompra> detalles = compraService.listaDetalles();
        Map<String, Producto> productos = productosPorId();

        // Datos que la vista necesita por compra: sus combustibles y sus litros
        Map<String, String> combustibles = new HashMap<>();
        Map<String, BigDecimal> litros = new HashMap<>();
        Set<String> productosComprados = new LinkedHashSet<>();
        BigDecimal totalMonto = BigDecimal.ZERO;
        BigDecimal totalLitros = BigDecimal.ZERO;

        for (Compra compra : compras) {
            if (compra.getTotal() != null) {
                totalMonto = totalMonto.add(compra.getTotal());
            }
        }
        for (DetalleCompra detalle : detalles) {
            totalLitros = totalLitros.add(detalle.getCantidad());
            Producto producto = productos.get(detalle.getIdProducto());
            String nombre = producto == null ? "" : producto.getNombre();
            if (producto != null) {
                productosComprados.add(nombre);
            }
            String anteriores = combustibles.get(detalle.getIdCompra());
            combustibles.put(detalle.getIdCompra(),
                    (anteriores == null || anteriores.isEmpty()) ? nombre : anteriores + ", " + nombre);
            BigDecimal previos = litros.get(detalle.getIdCompra());
            litros.put(detalle.getIdCompra(),
                    (previos == null ? BigDecimal.ZERO : previos).add(detalle.getCantidad()));
        }

        model.addAttribute("compras", compras);
        model.addAttribute("detalles", detalles);
        model.addAttribute("combustiblesPorCompra", combustibles);
        model.addAttribute("litrosPorCompra", litros);
        model.addAttribute("productosComprados", String.join(", ", productosComprados));
        model.addAttribute("totalMonto", totalMonto);
        model.addAttribute("totalLitros", totalLitros);
        return "compra/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("productos", productoService.listaProductos());
        model.addAttribute("responsables", responsables());
        return "compra/crear";
    }

    @PostMapping("/crear")
    public String crearCompra(@ModelAttribute("compra") Compra compra,
            @ModelAttribute("detalle") DetalleCompra detalle,
            @RequestParam(value = "fecha", required = false) LocalDate fecha,
            RedirectAttributes redirect) {
        String error = compraService.crearCompra(compra, detalle, fecha);
        if (error != null) {
            // Compra rechazada: se regresa al formulario sin modificar ningún dato
            redirect.addFlashAttribute("mensaje", error);
            return "redirect:/compras/crear";
        }
        redirect.addFlashAttribute("mensaje", "Compra " + compra.getId()
                + " registrada: " + detalle.getCantidad() + " L añadidos al stock.");
        return "redirect:/compras/list";
    }

    @GetMapping("/detalle")
    public String mostrarDetalle(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {
        Compra compra = compraService.buscarCompraPorId(id);
        if (compra == null) {
            // Id inexistente o ausente: nunca un error 500
            redirect.addFlashAttribute("mensaje", "No se encontró la compra solicitada.");
            return "redirect:/compras/list";
        }
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", compraService.listaDetallesPorCompra(compra.getId()));
        model.addAttribute("productosPorId", productosPorId());
        model.addAttribute("responsable", nombreResponsable(compra.getIdUsuario()));
        return "compra/detalle";
    }

    private Map<String, Producto> productosPorId() {
        Map<String, Producto> productos = new HashMap<>();
        for (Producto producto : productoService.listaProductos()) {
            productos.put(producto.getId(), producto);
        }
        return productos;
    }

    /**
     * Responsables del formulario: cada cuenta de usuario con el nombre
     * completo de su empleado (como los empleados Ana Torres, Luis Rojas y
     * Elena Díaz de la versión 1).
     */
    private Map<Integer, String> responsables() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Usuario usuario : usuarioService.listaUsuarios()) {
            nombres.put(usuario.getId(), nombreResponsable(usuario.getId()));
        }
        return nombres;
    }

    private String nombreResponsable(Integer idUsuario) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            return "";
        }
        Empleado empleado = empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado());
        if (empleado == null) {
            return usuario.getUsername();
        }
        return empleado.getNombres() + " " + empleado.getApellidos();
    }

}
