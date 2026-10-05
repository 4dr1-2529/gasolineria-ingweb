package com.example.nexo.controller;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
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

import com.example.nexo.model.Empleado;
import com.example.nexo.model.MovimientoInventario;
import com.example.nexo.model.Producto;
import com.example.nexo.model.Usuario;
import com.example.nexo.service.EmpleadoService;
import com.example.nexo.service.MovimientoInventarioService;
import com.example.nexo.service.ProductoService;
import com.example.nexo.service.UsuarioService;

/**
 * Inventario: existencias y libro de movimientos, consulta de entradas y
 * salidas, y los formularios de registro manual (P11–P14).
 * El controller sólo prepara datos para las vistas y delega el registro en
 * MovimientoInventarioService (RN01 y RN03).
 */
@Controller
@RequestMapping("/inventario")
public class InventarioController {

    /** Saldo físico de referencia al cierre del 09/09/2026 (versión 1). */
    private static final BigDecimal SALDO_INICIAL = new BigDecimal("6700");

    private final MovimientoInventarioService movimientoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public InventarioController(MovimientoInventarioService movimientoService, ProductoService productoService,
            UsuarioService usuarioService, EmpleadoService empleadoService) {
        this.movimientoService = movimientoService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    /**
     * P11 y P14 · Existencias de cada combustible y libro completo de
     * movimientos con su conciliación de saldo.
     */
    @GetMapping("/list")
    public String listarInventario(Model model) {
        List<MovimientoInventario> movimientos = movimientoService.listaMovimientos();

        BigDecimal entradas = BigDecimal.ZERO;
        BigDecimal salidas = BigDecimal.ZERO;
        for (MovimientoInventario movimiento : movimientos) {
            if ("Entrada".equals(movimiento.getTipoMovimiento())) {
                entradas = entradas.add(movimiento.getCantidad());
            } else {
                salidas = salidas.add(movimiento.getCantidad());
            }
        }

        BigDecimal totalFisico = BigDecimal.ZERO;
        for (Producto producto : productoService.listaProductos()) {
            totalFisico = totalFisico.add(producto.getStock());
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("productos", productoService.listaProductos());
        model.addAttribute("productosPorId", productosPorId());
        model.addAttribute("responsables", nombresUsuarios());
        model.addAttribute("entradas", entradas);
        model.addAttribute("salidas", salidas);
        model.addAttribute("saldoInicial", SALDO_INICIAL);
        model.addAttribute("totalFisico", totalFisico);
        model.addAttribute("bajos", existenciasBajas());
        return "inventario/lista";
    }

    /** P14 · Sólo las entradas del libro de movimientos. */
    @GetMapping("/entradas")
    public String listarEntradas(Model model) {
        return mostrarMovimientos(model, movimientoService.listaEntradas(), "inventario/entradas");
    }

    /** P14 · Sólo las salidas del libro de movimientos. */
    @GetMapping("/salidas")
    public String listarSalidas(Model model) {
        return mostrarMovimientos(model, movimientoService.listaSalidas(), "inventario/salidas");
    }

    @GetMapping("/entrada/crear")
    public String mostrarFormularioEntrada(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("productos", productoService.listaProductos());
        model.addAttribute("operadores", operadores());
        return "inventario/entrada";
    }

    @PostMapping("/entrada/crear")
    public String crearEntrada(@ModelAttribute("movimiento") MovimientoInventario movimiento,
            @RequestParam(value = "fechaHora", required = false) String fechaHora,
            RedirectAttributes redirect) {
        movimiento.setFechaHora(parseFechaHora(fechaHora));
        String error = movimientoService.registrarEntrada(movimiento);
        if (error != null) {
            // Entrada rechazada: se regresa al formulario sin modificar ningún dato
            redirect.addFlashAttribute("mensaje", error);
            return "redirect:/inventario/entrada/crear";
        }
        redirect.addFlashAttribute("mensaje", "Entrada " + movimiento.getId()
                + " registrada: " + movimiento.getCantidad() + " L añadidos al stock.");
        return "redirect:/inventario/list";
    }

    @GetMapping("/salida/crear")
    public String mostrarFormularioSalida(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("productos", productoService.listaProductos());
        model.addAttribute("operadores", operadores());
        return "inventario/salida";
    }

    @PostMapping("/salida/crear")
    public String crearSalida(@ModelAttribute("movimiento") MovimientoInventario movimiento,
            @RequestParam(value = "fechaHora", required = false) String fechaHora,
            RedirectAttributes redirect) {
        movimiento.setFechaHora(parseFechaHora(fechaHora));
        String error = movimientoService.registrarSalida(movimiento);
        if (error != null) {
            // Salida rechazada: se regresa al formulario sin modificar ningún dato
            redirect.addFlashAttribute("mensaje", error);
            return "redirect:/inventario/salida/crear";
        }
        redirect.addFlashAttribute("mensaje", "Salida " + movimiento.getId()
                + " registrada: " + movimiento.getCantidad() + " L descontados del stock.");
        return "redirect:/inventario/list";
    }

    private String mostrarMovimientos(Model model, List<MovimientoInventario> movimientos, String vista) {
        BigDecimal totalLitros = BigDecimal.ZERO;
        for (MovimientoInventario movimiento : movimientos) {
            totalLitros = totalLitros.add(movimiento.getCantidad());
        }
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("productosPorId", productosPorId());
        model.addAttribute("responsables", nombresUsuarios());
        model.addAttribute("totalLitros", totalLitros);
        return vista;
    }

    /**
     * El input datetime-local entrega la fecha en formato ISO (2026-10-04T08:30);
     * si está vacío o no se puede leer, la fecha queda nula y el Service
     * rechaza el movimiento.
     */
    private LocalDateTime parseFechaHora(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(texto.trim());
        } catch (DateTimeException e) {
            return null;
        }
    }

    private Map<String, Producto> productosPorId() {
        Map<String, Producto> productos = new HashMap<>();
        for (Producto producto : productoService.listaProductos()) {
            productos.put(producto.getId(), producto);
        }
        return productos;
    }

    /**
     * Responsables del formulario: las cuentas con rol "Operador / Vendedor"
     * (Ana Torres y Luis Rojas de la versión 1), con el nombre completo de
     * su empleado. Son los mismos responsables que ofrece P12 y P13.
     */
    private Map<Integer, String> operadores() {
        Map<Integer, String> nombres = new HashMap<>();
        for (Usuario usuario : usuarioService.listaUsuarios()) {
            if ("Operador / Vendedor".equals(usuario.getRol())) {
                nombres.put(usuario.getId(), nombreResponsable(usuario.getId()));
            }
        }
        return nombres;
    }

    /**
     * Todos los usuarios con nombre completo, para mostrar la columna
     * Responsable del libro de movimientos.
     */
    private Map<Integer, String> nombresUsuarios() {
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

    /**
     * Productos por debajo del umbral visual de 1,000 L de la versión 1,
     * por ejemplo "Gasolina Premium: 980 L". Vacío cuando no hay ninguno.
     */
    private String existenciasBajas() {
        StringBuilder texto = new StringBuilder();
        for (Producto producto : productoService.listaProductos()) {
            if (producto.getStock().compareTo(new BigDecimal("1000")) < 0) {
                if (texto.length() > 0) {
                    texto.append(", ");
                }
                texto.append(producto.getNombre()).append(": ").append(producto.getStock()).append(" L");
            }
        }
        return texto.toString();
    }

}
