package com.example.nexo.compra.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.compra.model.Compra;
import com.example.nexo.producto.model.Producto;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="detalle_compra")
@org.hibernate.annotations.DynamicUpdate
public class DetalleCompra {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_detalle_compra", nullable=false)
    private Long id;
    @Transient
    private String idCompra;
    @Transient
    private String idProducto;
    @Column(name="cantidad_litros", nullable=false, precision=12, scale=3)
    private BigDecimal cantidad;
    @Column(name="precio_compra", nullable=false, precision=12, scale=2)
    private BigDecimal precioCompra;
    @Column(name="subtotal", nullable=false, precision=12, scale=2)
    private BigDecimal subtotal;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_compra", nullable=false, unique=false)
    private Compra compra;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_producto", nullable=false, unique=false)
    private Producto producto;

    public DetalleCompra() { }
    public DetalleCompra(Long id, String idCompra, String idProducto, BigDecimal cantidad, BigDecimal precioCompra, BigDecimal subtotal) {
        this.id = id;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.subtotal = subtotal;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getIdCompra() { return idCompra != null ? idCompra : compra == null ? null : compra.getId(); }
    public void setIdCompra(String valor) { this.idCompra = valor; }
    public String getIdProducto() { return idProducto != null ? idProducto : producto == null ? null : producto.getId(); }
    public void setIdProducto(String valor) { this.idProducto = valor; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal valor) { this.cantidad = valor; }
    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal valor) { this.precioCompra = valor; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal valor) { this.subtotal = valor; }
    public Compra getCompra() { return compra; }
    public void setCompra(Compra valor) { this.compra = valor; this.idCompra = valor == null ? null : valor.getId(); }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto valor) { this.producto = valor; this.idProducto = valor == null ? null : valor.getId(); }
}
