package com.example.nexo.model;

/**
 * Concepto económico que clasifica a los movimientos de caja.
 * Atributos tomados del modelo de entidad de la versión 1:
 * id_concepto, nombre, tipo (Ingreso o Egreso) y estado.
 * La versión 1 sólo tiene dos conceptos: CE01 Venta de combustible
 * (Ingreso) y CE02 Compra de combustible (Egreso); no existen altas
 * de conceptos ni movimientos capturados a mano.
 */
public class ConceptoMovimiento {

    private String id;
    private String nombre;
    private String tipo;
    private String estado;

    public ConceptoMovimiento() {
        // Constructor vacío
    }

    public ConceptoMovimiento(String id, String nombre, String tipo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
