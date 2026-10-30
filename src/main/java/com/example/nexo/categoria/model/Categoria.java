package com.example.nexo.categoria.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="categoria", uniqueConstraints={@UniqueConstraint(columnNames={"nombre"})})
@org.hibernate.annotations.DynamicUpdate
public class Categoria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_categoria", nullable=false)
    private Long id;
    @Column(name="nombre", nullable=false, length=50)
    private String nombre;
    @Column(name="descripcion", nullable=true, length=200)
    private String descripcion;
    @Column(name="estado", nullable=false, length=10)
    private String estado;
    @Column(name="fecha_creacion", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaCreacion;

    public Categoria() { }
    public Categoria(Long id, String nombre, String descripcion, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String valor) { this.descripcion = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime valor) { this.fechaCreacion = valor; }
    @PrePersist void fechaInicial() { if (fechaCreacion == null) fechaCreacion = LocalDateTime.now(com.example.nexo.shared.config.TiempoConfig.LIMA); }
}
