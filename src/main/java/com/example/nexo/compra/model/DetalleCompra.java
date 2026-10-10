package com.example.nexo.compra.model;

import java.math.BigDecimal;

/**
 * Línea de una compra: producto recibido, litros, precio de compra y subtotal.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_detalle_compra, id_compra, id_producto, cantidad, precio_compra y subtotal.
 * cantidad son litros recibidos; precio_compra es el precio por litro en el
 * momento de la compra; subtotal = cantidad × precio_compra.
 */
public class DetalleCompra {

    private Integer id;
    private String idCompra;
    private String idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioCompra;
    private BigDecimal subtotal;

    public DetalleCompra() {
        // Constructor vacío
    }

    public DetalleCompra(Integer id, String idCompra, String idProducto, BigDecimal cantidad,
            BigDecimal precioCompra, BigDecimal subtotal) {
        this.id = id;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
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

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}
