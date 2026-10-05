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
