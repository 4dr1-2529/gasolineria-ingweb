package com.example.nexo.inventario.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.nexo.compra.model.Compra;
import com.example.nexo.venta.model.Venta;

/**
 * Movimiento físico de combustible: Entrada o Salida.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_movimiento_inventario, id_producto, id_usuario, tipo_movimiento,
 * cantidad, fecha_hora y motivo.
 * El motivo documenta el origen del movimiento: "Compra C001",
 * "Venta V001" o el texto libre de un ajuste manual.
 */
public class MovimientoInventario {

    private String id;
    private String idProducto;
    private Integer idUsuario;
    private String tipoMovimiento;
    private BigDecimal cantidad;
    private LocalDateTime fechaHora;
    private String motivo;

    public MovimientoInventario() {
        // Constructor vacío
    }

    public MovimientoInventario(String id, String idProducto, Integer idUsuario, String tipoMovimiento,
            BigDecimal cantidad, LocalDateTime fechaHora, String motivo) {
        this.id = id;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

}
