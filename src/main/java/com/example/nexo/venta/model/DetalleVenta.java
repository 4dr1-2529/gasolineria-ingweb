package com.example.nexo.venta.model;

import java.math.BigDecimal;

/**
 * Línea de una venta: producto vendido, litros, precio unitario y subtotal.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_detalle, id_venta, id_producto, cantidad, precio_unitario y subtotal.
 * cantidad son litros vendidos; precio_unitario es el precio aplicado en el
 * momento de la venta (precio histórico, independiente del precio actual);
 * subtotal = cantidad × precio_unitario.
 */
public class DetalleVenta {

    private Integer id;
    private String idVenta;
    private String idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleVenta() {
        // Constructor vacío
    }

    public DetalleVenta(Integer id, String idVenta, String idProducto, BigDecimal cantidad,
            BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}
