package com.example.nexo.venta.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.service.ProductoService;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;
import com.example.nexo.venta.model.DetalleVenta;
import com.example.nexo.venta.model.Venta;
import com.example.nexo.venta.service.VentaService;


/**
 * Ventas: consulta del historial, registro de la venta con su detalle y
 * vista de detalle. El controller sólo prepara datos para las vistas y
 * delega el registro en VentaService (RN01, RN02, RN03 y RN04).
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public VentaController(VentaService ventaService, ProductoService productoService,
            UsuarioService usuarioService, EmpleadoService empleadoService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/list")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.listaVentas();
        List<DetalleVenta> detalles = ventaService.listaDetalles();
        Map<String, Producto> productos = productosPorId();

        // Datos que la vista necesita por venta: su combustible y sus litros
        Map<String, String> combustibles = new HashMap<>();
        Map<String, BigDecimal> litros = new HashMap<>();
        BigDecimal totalMonto = BigDecimal.ZERO;
        BigDecimal totalLitros = BigDecimal.ZERO;

        for (Venta venta : ventas) {
            if (venta.getTotal() != null) {
                totalMonto = totalMonto.add(venta.getTotal());
            }
        }
        for (DetalleVenta detalle : detalles) {
            totalLitros = totalLitros.add(detalle.getCantidad());
            Producto producto = productos.get(detalle.getIdProducto());
            String nombre = producto == null ? "" : producto.getNombre();
            String anteriores = combustibles.get(detalle.getIdVenta());
            combustibles.put(detalle.getIdVenta(),
                    (anteriores == null || anteriores.isEmpty()) ? nombre : anteriores + ", " + nombre);
            BigDecimal previos = litros.get(detalle.getIdVenta());
            litros.put(detalle.getIdVenta(),
                    (previos == null ? BigDecimal.ZERO : previos).add(detalle.getCantidad()));
        }

        model.addAttribute("ventas", ventas);
        model.addAttribute("detalles", detalles);
        model.addAttribute("combustiblesPorVenta", combustibles);
        model.addAttribute("litrosPorVenta", litros);
        model.addAttribute("operadores", nombresUsuarios());
        model.addAttribute("totalMonto", totalMonto);
        model.addAttribute("totalLitros", totalLitros);
        return "venta/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        // Tras un rechazo conserva lo digitado (los atributos vienen por flash)
        if (!model.containsAttribute("venta")) {
            model.addAttribute("venta", new Venta());
        }
        if (!model.containsAttribute("detalle")) {
            model.addAttribute("detalle", new DetalleVenta());
        }
        // RN02 · Sólo los combustibles con estado Activo pueden venderse
        model.addAttribute("productos", productosActivos());
        model.addAttribute("operadores", operadores());
        return "venta/crear";
    }

    @PostMapping("/crear")
    public String crearVenta(@ModelAttribute("venta") Venta venta,
            @ModelAttribute("detalle") DetalleVenta detalle,
            RedirectAttributes redirect) {
        String error = ventaService.crearVenta(venta, detalle);
        if (error != null) {
            // Venta rechazada: se regresa al formulario conservando lo digitado
            redirect.addFlashAttribute("venta", venta);
            redirect.addFlashAttribute("detalle", detalle);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/ventas/crear";
        }
        redirect.addFlashAttribute("mensaje", "Venta " + venta.getId()
                + " registrada: " + detalle.getCantidad() + " L descontados del stock.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/ventas/list";
    }

    @GetMapping("/detalle")
    public String mostrarDetalle(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {
        Venta venta = ventaService.buscarVentaPorId(id);
        if (venta == null) {
            // Id inexistente o ausente: nunca un error 500
            redirect.addFlashAttribute("mensaje", "No se encontró la venta solicitada.");
            return "redirect:/ventas/list";
        }
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", ventaService.listaDetallesPorVenta(venta.getId()));
        model.addAttribute("productosPorId", productosPorId());
        model.addAttribute("operador", nombreOperador(venta.getIdUsuario()));
        return "venta/detalle";
    }

    private Map<String, Producto> productosPorId() {
        Map<String, Producto> productos = new HashMap<>();
        for (Producto producto : productoService.listaProductos()) {
            productos.put(producto.getId(), producto);
        }
        return productos;
    }

    /**
     * Combustibles que pueden elegirse en una nueva venta (RN02): sólo los
     * productos con estado Activo.
     */
    private List<Producto> productosActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productoService.listaProductos()) {
            if ("Activo".equals(producto.getEstado())) {
                activos.add(producto);
            }
        }
        return activos;
    }

    /**
     * Operadores del formulario: las cuentas con rol "Operador / Vendedor"
     * (Ana Torres y Luis Rojas de la versión 1), con el nombre completo de
     * su empleado. Son los mismos operadores que ofrece P08 y P24.
     */
    private Map<Integer, String> operadores() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Usuario usuario : usuarioService.listaUsuarios()) {
            // RN02: una cuenta Inactiva no se elige en operaciones nuevas
            if ("Operador / Vendedor".equals(usuario.getRol())
                    && "Activo".equals(usuario.getEstado())) {
                nombres.put(usuario.getId(), nombreOperador(usuario.getId()));
            }
        }
        return nombres;
    }

    /**
     * Todos los usuarios con nombre completo, para mostrar la columna
     * Operador del historial y el encabezado del detalle.
     */
    private Map<Integer, String> nombresUsuarios() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Usuario usuario : usuarioService.listaUsuarios()) {
            nombres.put(usuario.getId(), nombreOperador(usuario.getId()));
        }
        return nombres;
    }

    private String nombreOperador(Integer idUsuario) {
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
