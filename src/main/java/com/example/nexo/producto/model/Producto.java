package com.example.nexo.producto.model;

import java.math.BigDecimal;

public class Producto {

    private String id;
    private Integer idCategoria;
    private String nombre;
    private String unidadMedida;
    private BigDecimal precioActual;
    private BigDecimal stock;
    private String estado;

    public Producto() {
        // Constructor vacío
    }

    public Producto(String id, Integer idCategoria, String nombre, String unidadMedida,
                    BigDecimal precioActual, BigDecimal stock, String estado) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.precioActual = precioActual;
        this.stock = stock;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(BigDecimal precioActual) {
        this.precioActual = precioActual;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
