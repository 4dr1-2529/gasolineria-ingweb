package com.example.nexo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.nexo.model.ConceptoMovimiento;
import com.example.nexo.model.MovimientoCaja;

/**
 * Finanzas es de consulta: el servicio no recibe altas de movimientos.
 * Los movimientos se construyen a partir de las compras confirmadas
 * (egreso, RN04) y de las ventas confirmadas (ingreso, RN04), y el saldo
 * se calcula con BigDecimal a partir de la apertura de caja.
 */
public interface MovimientoCajaService {

    /** Todos los movimientos de caja, en orden de ocurrencia. */
    public List<MovimientoCaja> listaMovimientos();

    /** Sólo los ingresos: los movimientos nacidos de las ventas. */
    public List<MovimientoCaja> listaIngresos();

    /** Sólo los egresos: los movimientos nacidos de las compras. */
    public List<MovimientoCaja> listaEgresos();

    /** El movimiento con ese código (MC001...), o null si no existe. */
    public MovimientoCaja buscarMovimientoPorId(String id);

    /** Los conceptos económicos de la versión 1: CE01 y CE02. */
    public List<ConceptoMovimiento> listaConceptos();

    /** Apertura de caja de referencia (constante de la versión 1). */
    public BigDecimal apertura();

    /** Suma de los montos de ingreso. */
    public BigDecimal totalIngresos();

    /** Suma de los montos de egreso. */
    public BigDecimal totalEgresos();

    /** Saldo = apertura + ingresos − egresos (RN04). */
    public BigDecimal saldoActual();

}
