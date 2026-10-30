package com.example.nexo.finanzas.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="concepto_movimiento", uniqueConstraints={@UniqueConstraint(columnNames={"codigo"}),@UniqueConstraint(columnNames={"nombre"})})
@org.hibernate.annotations.DynamicUpdate
public class ConceptoMovimiento {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_concepto", nullable=false)
    private Long clave;
    @Column(name="codigo", nullable=false, length=10)
    private String id;
    @Column(name="nombre", nullable=false, length=60)
    private String nombre;
    @Column(name="tipo", nullable=false, length=10)
    @Convert(converter=TipoMovimientoConverter.class)
    private String tipo;
    @Column(name="estado", nullable=false, length=10)
    private String estado;

    public ConceptoMovimiento() { }
    public ConceptoMovimiento(String id, String nombre, String tipo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }
    public Long getClave() { return clave; }
    public void setClave(Long valor) { this.clave = valor; }
    public String getId() { return id; }
    public void setId(String valor) { this.id = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getTipo() { return tipo; }
    public void setTipo(String valor) { this.tipo = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
}
