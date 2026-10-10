package com.example.nexo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Compra;
import com.example.nexo.model.ConceptoMovimiento;
import com.example.nexo.model.MovimientoCaja;
import com.example.nexo.model.Venta;

/**
 * Movimientos de caja en memoria, sin Repository ni base de datos.
 * La fuente única es CompraService y VentaService: cada compra
 * confirmada produce un único egreso por su total (RN04) y cada venta
 * confirmada produce un único ingreso por su total (RN04). Aquí no se
 * crean movimientos a mano ni se modifica el stock de los productos.
 */
@Service
public class MovimientoCajaServiceImpl implements MovimientoCajaService {

    /** Apertura de caja del 10/09/2026 tomada de la versión 1. */
    private static final BigDecimal APERTURA = new BigDecimal("4410.00");

    private final CompraService compraService;
    private final VentaService ventaService;
    private final List<ConceptoMovimiento> conceptos = new ArrayList<>();

    public MovimientoCajaServiceImpl(CompraService compraService, VentaService ventaService) {
        this.compraService = compraService;
        this.ventaService = ventaService;
        // Únicos conceptos económicos de la versión 1, ambos activos
        conceptos.add(new ConceptoMovimiento("CE01", "Venta de combustible", "Ingreso", "Activo"));
        conceptos.add(new ConceptoMovimiento("CE02", "Compra de combustible", "Egreso", "Activo"));
    }

    public List<MovimientoCaja> listaMovimientos() {
        List<MovimientoCaja> movimientos = new ArrayList<>();

        // RN04 · Cada compra confirmada aporta su único egreso por el total
        for (Compra compra : compraService.listaCompras()) {
            if ("Confirmada".equals(compra.getEstado())) {
                MovimientoCaja movimiento = new MovimientoCaja();
                movimiento.setIdConcepto("CE02");
                movimiento.setIdUsuario(compra.getIdUsuario());
                movimiento.setIdCompra(compra.getId());
                movimiento.setTipo("Egreso");
                movimiento.setMonto(compra.getTotal());
                movimiento.setDescripcion("Compra de combustible");
                movimiento.setFechaHora(compra.getFechaHora());
                movimientos.add(movimiento);
            }
        }

        // RN04 · Cada venta confirmada aporta su único ingreso por el total
        for (Venta venta : ventaService.listaVentas()) {
            if ("Confirmada".equals(venta.getEstado())) {
                MovimientoCaja movimiento = new MovimientoCaja();
                movimiento.setIdConcepto("CE01");
                movimiento.setIdUsuario(venta.getIdUsuario());
                movimiento.setIdVenta(venta.getId());
                movimiento.setTipo("Ingreso");
                movimiento.setMonto(venta.getTotal());
                movimiento.setDescripcion("Venta de combustible");
                movimiento.setFechaHora(venta.getFechaHora());
                movimientos.add(movimiento);
            }
        }

        // En la versión 1 los hechos ocurren en orden: la compra C001 a las
        // 07:00 es MC001 y las ventas de las 09:00, 10:00 y 11:00 son
        // MC003, MC004 y MC005 (la numeración salta los dos códigos que la
        // versión corregida ya no usa)
        Collections.sort(movimientos, Comparator.comparing(MovimientoCaja::getFechaHora));
        for (int i = 0; i < movimientos.size(); i++) {
            movimientos.get(i).setId(siguienteCodigo(i));
        }
        return movimientos;
    }

    public List<MovimientoCaja> listaIngresos() {
        return porTipo("Ingreso");
    }

    public List<MovimientoCaja> listaEgresos() {
        return porTipo("Egreso");
    }

    public MovimientoCaja buscarMovimientoPorId(String id) {
        if (id == null) {
            return null;
        }
        for (MovimientoCaja movimiento : listaMovimientos()) {
            if (id.equals(movimiento.getId())) {
                return movimiento;
            }
        }
        return null;
    }

    public List<ConceptoMovimiento> listaConceptos() {
        return conceptos;
    }

    public ConceptoMovimiento buscarConceptoPorId(String id) {
        if (id == null) {
            return null;
        }
        for (ConceptoMovimiento concepto : conceptos) {
            if (id.equals(concepto.getId())) {
                return concepto;
            }
        }
        return null;
    }

    public String crearConcepto(ConceptoMovimiento concepto) {
        String error = validarConcepto(concepto, null);
        if (error != null) {
            return error;
        }
        normalizarConcepto(concepto);
        concepto.setId(siguienteCodigoConcepto());
        conceptos.add(concepto);
        return null; // alta completada
    }

    public String editarConcepto(ConceptoMovimiento concepto) {
        ConceptoMovimiento existente = buscarConceptoPorId(concepto == null ? null : concepto.getId());
        if (existente == null) {
            return "No se encontró el concepto solicitado.";
        }
        String error = validarConcepto(concepto, concepto.getId());
        if (error != null) {
            return error;
        }
        normalizarConcepto(concepto);
        existente.setNombre(concepto.getNombre());
        existente.setTipo(concepto.getTipo());
        existente.setEstado(concepto.getEstado());
        return null; // edición completada: se conserva el código y el historial
    }

    public String cambiarEstadoConcepto(String id, String estado) {
        ConceptoMovimiento concepto = buscarConceptoPorId(id);
        if (concepto == null) {
            return "No se encontró el concepto solicitado.";
        }
        if (!esEstadoValido(estado)) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // RN02: el concepto se conserva con su nuevo estado; no se elimina
        concepto.setEstado(estado);
        return null;
    }

    /**
     * Reglas comunes de alta y edición (F23 y F25): nombre obligatorio de 3
     * a 40 caracteres, único ignorando mayúsculas y espacios; tipo del
     * catálogo (Ingreso / Egreso); estado del catálogo (Activo / Inactivo).
     * 'idExcluido' es el código que no compite consigo mismo al unicidad.
     */
    private String validarConcepto(ConceptoMovimiento concepto, String idExcluido) {
        if (concepto == null || concepto.getNombre() == null
                || concepto.getNombre().trim().isEmpty()) {
            return "El nombre del concepto es obligatorio.";
        }
        String nombre = concepto.getNombre().trim();
        if (nombre.length() < 3 || nombre.length() > 40) {
            return "El nombre del concepto debe tener entre 3 y 40 caracteres.";
        }
        // Unicidad ignorando mayúsculas y espacios (como en los demás catálogos)
        String normalizado = normalizarTexto(nombre);
        for (ConceptoMovimiento existente : conceptos) {
            boolean mismo = idExcluido != null && idExcluido.equals(existente.getId());
            if (!mismo && normalizado.equals(normalizarTexto(existente.getNombre()))) {
                return "Ya existe un concepto con el nombre «" + existente.getNombre() + "».";
            }
        }
        if (!"Ingreso".equals(concepto.getTipo()) && !"Egreso".equals(concepto.getTipo())) {
            return "El tipo debe ser Ingreso o Egreso.";
        }
        if (!esEstadoValido(concepto.getEstado())) {
            return "El estado debe ser Activo o Inactivo.";
        }
        return null; // sin errores
    }

    private boolean esEstadoValido(String estado) {
        return "Activo".equals(estado) || "Inactivo".equals(estado);
    }

    /** Guarda el nombre sin los espacios sobrantes. */
    private void normalizarConcepto(ConceptoMovimiento concepto) {
        concepto.setNombre(concepto.getNombre().trim());
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    /** Continúa la numeración de códigos: CE01, CE02 → CE03. */
    private String siguienteCodigoConcepto() {
        int maximo = 0;
        for (ConceptoMovimiento concepto : conceptos) {
            String id = concepto.getId();
            if (id != null && id.startsWith("CE")) {
                try {
                    int numero = Integer.parseInt(id.substring(2));
                    if (numero > maximo) {
                        maximo = numero;
                    }
                } catch (NumberFormatException excepcion) {
                    // Un código fuera del patrón no altera la numeración
                }
            }
        }
        return String.format("CE%02d", maximo + 1);
    }

    public BigDecimal apertura() {
        return APERTURA;
    }

    public BigDecimal totalIngresos() {
        return sumar(listaIngresos());
    }

    public BigDecimal totalEgresos() {
        return sumar(listaEgresos());
    }

    public BigDecimal saldoActual() {
        return APERTURA.add(totalIngresos()).subtract(totalEgresos());
    }

    private List<MovimientoCaja> porTipo(String tipo) {
        List<MovimientoCaja> filtrados = new ArrayList<>();
        for (MovimientoCaja movimiento : listaMovimientos()) {
            if (tipo.equals(movimiento.getTipo())) {
                filtrados.add(movimiento);
            }
        }
        return filtrados;
    }

    private BigDecimal sumar(List<MovimientoCaja> movimientos) {
        BigDecimal total = BigDecimal.ZERO;
        for (MovimientoCaja movimiento : movimientos) {
            total = total.add(movimiento.getMonto());
        }
        return total;
    }

    /**
     * Código de la posición 0 en adelante: MC001, MC003, MC004, MC005,
     * MC007... Se saltan dos posiciones porque la versión 1 corregida
     * eliminó esos códigos de la numeración.
     */
    private String siguienteCodigo(int posicion) {
        int numero = posicion + 1;
        if (numero >= 2) {
            numero++;
        }
        if (numero >= 6) {
            numero++;
        }
        return String.format("MC%03d", numero);
    }

}
