package com.example.nexo.producto.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.categoria.model.Categoria;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="producto", uniqueConstraints={@UniqueConstraint(columnNames={"codigo_producto"}),@UniqueConstraint(columnNames={"id_categoria","nombre"})})
@org.hibernate.annotations.DynamicUpdate
public class Producto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_producto", nullable=false)
    private Long clave;
    @Column(name="codigo_producto", nullable=false, length=10)
    private String id;
    @Transient
    private Long idCategoria;
    @Column(name="nombre", nullable=false, length=60)
    private String nombre;
    @Column(name="unidad_medida", nullable=false, length=10)
    private String unidadMedida;
    @Column(name="precio_venta", nullable=false, precision=12, scale=2)
    private BigDecimal precioActual;
    @Column(name="stock_actual", nullable=false, precision=12, scale=3)
    private BigDecimal stock;
    @Column(name="estado", nullable=false, length=10)
    private String estado;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_categoria", nullable=false, unique=false)
    private Categoria categoria;

    public Producto() { }
    public Producto(String id, Long idCategoria, String nombre, String unidadMedida, BigDecimal precioActual, BigDecimal stock, String estado) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.precioActual = precioActual;
        this.stock = stock;
        this.estado = estado;
    }
    public Long getClave() { return clave; }
    public void setClave(Long valor) { this.clave = valor; }
    public String getId() { return id; }
    public void setId(String valor) { this.id = valor; }
    public Long getIdCategoria() { return idCategoria != null ? idCategoria : categoria == null ? null : categoria.getId(); }
    public void setIdCategoria(Long valor) { this.idCategoria = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String valor) { this.unidadMedida = valor; }
    public BigDecimal getPrecioActual() { return precioActual; }
    public void setPrecioActual(BigDecimal valor) { this.precioActual = valor; }
    public BigDecimal getStock() { return stock; }
    public void setStock(BigDecimal valor) { this.stock = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria valor) { this.categoria = valor; this.idCategoria = valor == null ? null : valor.getId(); }
}
