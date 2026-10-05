package com.example.nexo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nexo.model.ConceptoMovimiento;
import com.example.nexo.model.Empleado;
import com.example.nexo.model.MovimientoCaja;
import com.example.nexo.model.Usuario;
import com.example.nexo.service.EmpleadoService;
import com.example.nexo.service.MovimientoCajaService;
import com.example.nexo.service.UsuarioService;

/**
 * Finanzas: consulta de los movimientos de caja, de sus ingresos y
 * egresos y del detalle de cada movimiento (P15, P16 y P30).
 * No hay formularios de alta: los movimientos nacen de las compras
 * (egreso, RN04) y de las ventas (ingreso, RN04) y el saldo se calcula
 * con BigDecimal como apertura + ingresos − egresos.
 */
@Controller
@RequestMapping("/finanzas")
public class FinanzasController {

    private final MovimientoCajaService movimientoService;
    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public FinanzasController(MovimientoCajaService movimientoService, UsuarioService usuarioService,
            EmpleadoService empleadoService) {
        this.movimientoService = movimientoService;
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    /**
     * P15 · Resumen financiero: KPI de ingresos, egresos y saldo con su
     * apertura, más el listado completo de movimientos de caja.
     */
    @GetMapping("/list")
    public String resumen(Model model) {
        model.addAttribute("movimientos", movimientoService.listaMovimientos());
        model.addAttribute("conceptosPorId", conceptosPorId());
        model.addAttribute("responsables", nombresUsuarios());
        agregarConciliacion(model);
        return "finanzas/lista";
    }

    /** P16 · Sólo los ingresos de caja, generados por las ventas (F26). */
    @GetMapping("/ingresos")
    public String listarIngresos(Model model) {
        return mostrarMovimientos(model, movimientoService.listaIngresos(),
                movimientoService.totalIngresos(), "finanzas/ingresos");
    }

    /** P16 · Sólo los egresos de caja, generados por las compras (F27). */
    @GetMapping("/egresos")
    public String listarEgresos(Model model) {
        return mostrarMovimientos(model, movimientoService.listaEgresos(),
                movimientoService.totalEgresos(), "finanzas/egresos");
    }

    /**
     * P30 · Detalle de un movimiento con su conciliación de caja.
     * Un código inexistente vuelve al resumen con aviso: nunca un 500.
     */
    @GetMapping("/detalle")
    public String detalle(@RequestParam("id") String id, Model model, RedirectAttributes redirect) {
        MovimientoCaja movimiento = movimientoService.buscarMovimientoPorId(id);
        if (movimiento == null) {
            redirect.addFlashAttribute("mensaje", "El movimiento " + id + " no existe en caja.");
            return "redirect:/finanzas/list";
        }
        model.addAttribute("movimiento", movimiento);
        model.addAttribute("concepto", conceptosPorId().get(movimiento.getIdConcepto()));
        model.addAttribute("responsable", nombreResponsable(movimiento.getIdUsuario()));
        model.addAttribute("cantidad", movimientoService.listaMovimientos().size());
        agregarConciliacion(model);
        return "finanzas/detalle";
    }

    private String mostrarMovimientos(Model model, List<MovimientoCaja> movimientos, BigDecimal total,
            String vista) {
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("conceptosPorId", conceptosPorId());
        model.addAttribute("responsables", nombresUsuarios());
        model.addAttribute("total", total);
        agregarConciliacion(model);
        return vista;
    }

    /** Datos comunes de la conciliación: apertura + ingresos − egresos = saldo. */
    private void agregarConciliacion(Model model) {
        model.addAttribute("apertura", movimientoService.apertura());
        model.addAttribute("ingresos", movimientoService.totalIngresos());
        model.addAttribute("egresos", movimientoService.totalEgresos());
        model.addAttribute("saldo", movimientoService.saldoActual());
        model.addAttribute("origenesIngresos", origenes(movimientoService.listaIngresos()));
        model.addAttribute("origenesEgresos", origenes(movimientoService.listaEgresos()));
    }

    /** Orígenes de los movimientos (V001, V002, V003 o C001) para los avisos. */
    private String origenes(List<MovimientoCaja> movimientos) {
        StringBuilder texto = new StringBuilder();
        for (MovimientoCaja movimiento : movimientos) {
            if (texto.length() > 0) {
                texto.append(", ");
            }
            if (movimiento.getIdVenta() != null) {
                texto.append(movimiento.getIdVenta());
            } else {
                texto.append(movimiento.getIdCompra());
            }
        }
        return texto.toString();
    }

    private Map<String, ConceptoMovimiento> conceptosPorId() {
        Map<String, ConceptoMovimiento> conceptos = new HashMap<>();
        for (ConceptoMovimiento concepto : movimientoService.listaConceptos()) {
            conceptos.put(concepto.getId(), concepto);
        }
        return conceptos;
    }

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

}
