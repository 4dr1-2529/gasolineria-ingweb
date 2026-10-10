package com.example.nexo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.nexo.model.ConceptoMovimiento;
import com.example.nexo.model.MovimientoCaja;

/**
 * Finanzas no recibe altas de movimientos: los movimientos se construyen
 * a partir de las compras confirmadas (egreso, RN04) y de las ventas
 * confirmadas (ingreso, RN04), y el saldo se calcula con BigDecimal a
 * partir de la apertura de caja.
 * El catálogo de conceptos económicos sí admite escritura (F23 registrar,
 * F24 consultar, F25 editar y cambiar estado), con validaciones de servidor.
 * Cada método de escritura devuelve un mensaje de error en español, o
 * null si la operación se completó: el controller lo convierte en aviso.
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

    /** F24 · Busca un concepto por su código (CE01…); null si no existe. */
    public ConceptoMovimiento buscarConceptoPorId(String id);

    /** F23 · Alta de un concepto; devuelve el error o null si se registró. */
    public String crearConcepto(ConceptoMovimiento concepto);

    /** F25 · Edición conservando el código; devuelve el error o null. */
    public String editarConcepto(ConceptoMovimiento concepto);

    /** F25 · RN02 · cambia el estado sin eliminar el registro. */
    public String cambiarEstadoConcepto(String id, String estado);

    /** Apertura de caja de referencia (constante de la versión 1). */
    public BigDecimal apertura();

    /** Suma de los montos de ingreso. */
    public BigDecimal totalIngresos();

    /** Suma de los montos de egreso. */
    public BigDecimal totalEgresos();

    /** Saldo = apertura + ingresos − egresos (RN04). */
    public BigDecimal saldoActual();

}
