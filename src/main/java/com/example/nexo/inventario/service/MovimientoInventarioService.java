package com.example.nexo.inventario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.nexo.inventario.model.MovimientoInventario;


public interface MovimientoInventarioService {

    public List<MovimientoInventario> listaMovimientos();

    public List<MovimientoInventario> listaEntradas();

    public List<MovimientoInventario> listaSalidas();

    public MovimientoInventario buscarMovimientoPorId(String id);

    /**
     * P12 · Entrada manual de combustible (RN03): valida y suma los
     * litros al stock. Devuelve null si la entrada es válida o, si no, el
     * motivo del rechazo.
     */
    public String registrarEntrada(MovimientoInventario movimiento);

    /**
     * P13 · Salida manual de combustible (RN01 y RN03): valida y descuenta
     * los litros del stock. Devuelve null si la salida es válida o, si no,
     * el motivo del rechazo.
     */
    public String registrarSalida(MovimientoInventario movimiento);

    /**
     * RN03 · La compra ya sumó los litros al stock; aquí sólo se registra
     * la entrada correspondiente, sin volver a modificar el stock.
     */
    public void registrarEntradaPorCompra(String idProducto, Integer idUsuario, BigDecimal cantidad,
            LocalDateTime fechaHora, String motivo);

    /**
     * RN03 · La venta ya descontó los litros del stock; aquí sólo se
     * registra la salida correspondiente, sin volver a modificar el stock.
     */
    public void registrarSalidaPorVenta(String idProducto, Integer idUsuario, BigDecimal cantidad,
            LocalDateTime fechaHora, String motivo);

}
