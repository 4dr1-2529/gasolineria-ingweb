package com.example.nexo.usuario.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="rol", uniqueConstraints={@UniqueConstraint(columnNames={"codigo"}),@UniqueConstraint(columnNames={"nombre"})})
@org.hibernate.annotations.DynamicUpdate
public class Rol {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_rol", nullable=false)
    private Long id;
    @Column(name="codigo", nullable=false, length=20)
    private String codigo;
    @Column(name="nombre", nullable=false, length=40)
    private String nombre;
    @Column(name="descripcion", nullable=false, length=200)
    private String descripcion;
    @Column(name="estado", nullable=false, length=10)
    private String estado;

    public Rol() { }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String valor) { this.codigo = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String valor) { this.descripcion = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
}
