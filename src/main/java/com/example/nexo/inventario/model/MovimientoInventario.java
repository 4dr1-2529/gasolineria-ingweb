package com.example.nexo.inventario.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.compra.model.DetalleCompra;
import com.example.nexo.venta.model.DetalleVenta;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="movimiento_inventario", uniqueConstraints={@UniqueConstraint(columnNames={"id_detalle_compra"}),@UniqueConstraint(columnNames={"id_detalle_venta"})})
@org.hibernate.annotations.DynamicUpdate
public class MovimientoInventario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_movimiento_inventario", nullable=false)
    private Long clave;
    @Transient
    private String id;
    @Transient
    private String idProducto;
    @Transient
    private Long idUsuario;
    @Column(name="tipo", nullable=false, length=10)
    @Convert(converter=TipoMovimientoConverter.class)
    private String tipoMovimiento;
    @Column(name="cantidad_litros", nullable=false, precision=12, scale=3)
    private BigDecimal cantidad;
    @Column(name="fecha_hora", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaHora;
    @Column(name="motivo", nullable=false, length=200)
    private String motivo;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_producto", nullable=false, unique=false)
    private Producto producto;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_usuario", nullable=false, unique=false)
    private Usuario usuario;
    @OneToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_detalle_compra", nullable=true, unique=true)
    private DetalleCompra detalleCompra;
    @OneToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_detalle_venta", nullable=true, unique=true)
    private DetalleVenta detalleVenta;

    public MovimientoInventario() { }
    public MovimientoInventario(String id, String idProducto, Long idUsuario, String tipoMovimiento, BigDecimal cantidad, LocalDateTime fechaHora, String motivo) {
        this.id = id;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
    }
    public Long getClave() { return clave; }
    public void setClave(Long valor) { this.clave = valor; }
    public String getId() { return clave == null ? id : String.format("MI%03d", clave); }
    public void setId(String valor) { this.id = valor; }
    public String getIdProducto() { return idProducto != null ? idProducto : producto == null ? null : producto.getId(); }
    public void setIdProducto(String valor) { this.idProducto = valor; }
    public Long getIdUsuario() { return idUsuario != null ? idUsuario : usuario == null ? null : usuario.getId(); }
    public void setIdUsuario(Long valor) { this.idUsuario = valor; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String valor) { this.tipoMovimiento = valor; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal valor) { this.cantidad = valor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime valor) { this.fechaHora = valor; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String valor) { this.motivo = valor; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto valor) { this.producto = valor; this.idProducto = valor == null ? null : valor.getId(); }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario valor) { this.usuario = valor; this.idUsuario = valor == null ? null : valor.getId(); }
    public DetalleCompra getDetalleCompra() { return detalleCompra; }
    public void setDetalleCompra(DetalleCompra valor) { this.detalleCompra = valor; }
    public DetalleVenta getDetalleVenta() { return detalleVenta; }
    public void setDetalleVenta(DetalleVenta valor) { this.detalleVenta = valor; }
}
