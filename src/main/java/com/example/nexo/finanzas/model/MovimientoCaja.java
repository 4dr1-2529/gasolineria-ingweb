package com.example.nexo.finanzas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Movimiento de caja: un ingreso o un egreso económico.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_movimiento_caja, id_concepto, id_usuario, id_venta (opcional),
 * id_compra (opcional), tipo, monto, descripcion y fecha_hora.
 * Cada movimiento nace de una venta (ingreso, RN04) o de una compra
 * (egreso, RN04): id_venta y id_compra nunca vienen juntos.
 */
public class MovimientoCaja {

    private String id;
    private String idConcepto;
    private Integer idUsuario;
    private String idVenta;
    private String idCompra;
    private String tipo;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaHora;

    public MovimientoCaja() {
        // Constructor vacío
    }

    public MovimientoCaja(String id, String idConcepto, Integer idUsuario, String idVenta,
            String idCompra, String tipo, BigDecimal monto, String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.idConcepto = idConcepto;
        this.idUsuario = idUsuario;
        this.idVenta = idVenta;
        this.idCompra = idCompra;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(String idConcepto) {
        this.idConcepto = idConcepto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

}
