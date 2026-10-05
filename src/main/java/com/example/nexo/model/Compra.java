package com.example.nexo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Compra de combustible al proveedor.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_compra, id_usuario (responsable), fecha_hora, proveedor, total y estado.
 * El proveedor es un atributo de texto, no una entidad.
 */
public class Compra {

    private String id;
    private Integer idUsuario;
    private LocalDateTime fechaHora;
    private String proveedor;
    private BigDecimal total;
    private String estado;

    public Compra() {
        // Constructor vacío
    }

    public Compra(String id, Integer idUsuario, LocalDateTime fechaHora, String proveedor,
            BigDecimal total, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaHora = fechaHora;
        this.proveedor = proveedor;
        this.total = total;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
