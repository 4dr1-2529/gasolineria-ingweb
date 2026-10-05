package com.example.nexo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Venta de combustible.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_venta, id_usuario (operador), fecha_hora, total y estado.
 * El total es la suma de los subtotales de sus líneas de detalle.
 */
public class Venta {

    private String id;
    private Integer idUsuario;
    private LocalDateTime fechaHora;
    private BigDecimal total;
    private String estado;

    public Venta() {
        // Constructor vacío
    }

    public Venta(String id, Integer idUsuario, LocalDateTime fechaHora, BigDecimal total, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaHora = fechaHora;
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
