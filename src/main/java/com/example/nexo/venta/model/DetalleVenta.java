package com.example.nexo.venta.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.venta.model.Venta;
import com.example.nexo.producto.model.Producto;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="detalle_venta")
@org.hibernate.annotations.DynamicUpdate
public class DetalleVenta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_detalle_venta", nullable=false)
    private Long id;
    @Transient
    private String idVenta;
    @Transient
    private String idProducto;
    @Column(name="cantidad_litros", nullable=false, precision=12, scale=3)
    private BigDecimal cantidad;
    @Column(name="precio_unitario", nullable=false, precision=12, scale=2)
    private BigDecimal precioUnitario;
    @Column(name="subtotal", nullable=false, precision=12, scale=2)
    private BigDecimal subtotal;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_venta", nullable=false, unique=false)
    private Venta venta;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_producto", nullable=false, unique=false)
    private Producto producto;

    public DetalleVenta() { }
    public DetalleVenta(Long id, String idVenta, String idProducto, BigDecimal cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getIdVenta() { return idVenta != null ? idVenta : venta == null ? null : venta.getId(); }
    public void setIdVenta(String valor) { this.idVenta = valor; }
    public String getIdProducto() { return idProducto != null ? idProducto : producto == null ? null : producto.getId(); }
    public void setIdProducto(String valor) { this.idProducto = valor; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal valor) { this.cantidad = valor; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal valor) { this.precioUnitario = valor; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal valor) { this.subtotal = valor; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta valor) { this.venta = valor; this.idVenta = valor == null ? null : valor.getId(); }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto valor) { this.producto = valor; this.idProducto = valor == null ? null : valor.getId(); }
}
