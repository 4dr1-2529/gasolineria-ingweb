package com.example.nexo.finanzas.controller;

import java.math.BigDecimal;
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
import com.example.nexo.finanzas.model.ConceptoMovimiento;
import com.example.nexo.finanzas.model.MovimientoCaja;
import com.example.nexo.finanzas.service.MovimientoCajaService;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;


/**
 * Finanzas: consulta de los movimientos de caja, de sus ingresos y
 * egresos y del detalle de cada movimiento (P15, P16 y P30), más el
 * CRUD del catálogo de conceptos económicos (F23–F25).
 * No hay formularios de alta de movimientos: los movimientos nacen de
 * las compras (egreso, RN04) y de las ventas (ingreso, RN04) y el saldo
 * se calcula con BigDecimal como apertura + ingresos − egresos.
 * Cualquier validación fallida devuelve al formulario con el aviso del
 * servidor y conserva lo digitado; el éxito redirige al listado.
 * RN02: no hay ninguna ruta de eliminación, sólo cambio de estado (F25).
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

    /** F24 · P17 · Catálogo de conceptos con su tipo y estado. */
    @GetMapping("/conceptos/list")
    public String listarConceptos(Model model) {
        model.addAttribute("conceptos", movimientoService.listaConceptos());
        // Los conceptos con movimientos asociados se conservan (RN02)
        model.addAttribute("cantidadMovimientos", movimientoService.listaMovimientos().size());
        return "finanzas/conceptos"; // Retorna la vista correspondiente
    }

    @GetMapping("/conceptos/crear")
    public String mostrarFormularioCrearConcepto(Model model) {
        // Se agrega un objeto vacío al modelo para que el formulario pueda vincularse
        if (!model.containsAttribute("concepto")) {
            model.addAttribute("concepto", new ConceptoMovimiento());
        }
        return "finanzas/concepto-crear"; // Retorna la vista correspondiente
    }

    @PostMapping("/conceptos/crear")
    public String crearConcepto(@ModelAttribute("concepto") ConceptoMovimiento concepto,
            RedirectAttributes redirect) {
        String error = movimientoService.crearConcepto(concepto);
        if (error != null) {
            // Se conserva lo digitado y se muestra el motivo del servidor
            redirect.addFlashAttribute("concepto", concepto);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/finanzas/conceptos/crear";
        }
        redirect.addFlashAttribute("mensaje", "El concepto se registró correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/finanzas/conceptos/list";
    }

    @GetMapping("/conceptos/editar")
    public String mostrarFormularioEditarConcepto(@RequestParam("id") String id, Model model,
            RedirectAttributes redirect) {
        ConceptoMovimiento concepto = movimientoService.buscarConceptoPorId(id);
        if (concepto == null) {
            redirect.addFlashAttribute("mensaje", "No se encontró el concepto solicitado.");
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/finanzas/conceptos/list";
        }
        model.addAttribute("concepto", concepto);
        return "finanzas/concepto-editar"; // Retorna la vista correspondiente
    }

    @PostMapping("/conceptos/editar")
    public String editarConcepto(@ModelAttribute("concepto") ConceptoMovimiento concepto,
            RedirectAttributes redirect) {
        String error = movimientoService.editarConcepto(concepto);
        if (error != null) {
            redirect.addFlashAttribute("concepto", concepto);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/finanzas/conceptos/editar?id=" + concepto.getId();
        }
        redirect.addFlashAttribute("mensaje", "El concepto se editó correctamente.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/finanzas/conceptos/list";
    }

    /**
     * F25 · RN02: cambiar estado sin eliminar. El código viaja oculto en el
     * formulario del listado; el estado lo decide el botón pulsado.
     */
    @PostMapping("/conceptos/estado")
    public String cambiarEstadoConcepto(@RequestParam("id") String id,
            @RequestParam("estado") String estado, RedirectAttributes redirect) {
        String error = movimientoService.cambiarEstadoConcepto(id, estado);
        if (error != null) {
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
        } else {
            redirect.addFlashAttribute("mensaje",
                    "El concepto quedó " + estado + " (el registro se conserva, RN02).");
            redirect.addFlashAttribute("mensajeTipo", "ok");
        }
        return "redirect:/finanzas/conceptos/list";
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
