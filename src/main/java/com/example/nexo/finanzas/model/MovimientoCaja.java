package com.example.nexo.finanzas.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.finanzas.model.ConceptoMovimiento;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.compra.model.Compra;
import com.example.nexo.venta.model.Venta;
import com.example.nexo.finanzas.model.CajaDiaria;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="movimiento_caja", uniqueConstraints={@UniqueConstraint(columnNames={"codigo_movimiento"}),@UniqueConstraint(columnNames={"id_compra"}),@UniqueConstraint(columnNames={"id_venta"})})
@org.hibernate.annotations.DynamicUpdate
public class MovimientoCaja {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_movimiento_caja", nullable=false)
    private Long clave;
    @Column(name="codigo_movimiento", nullable=false, length=12)
    private String id;
    @Transient
    private String idConcepto;
    @Transient
    private Long idUsuario;
    @Transient
    private String idVenta;
    @Transient
    private String idCompra;
    @Column(name="tipo", nullable=false, length=10)
    @Convert(converter=TipoMovimientoConverter.class)
    private String tipo;
    @Column(name="monto", nullable=false, precision=12, scale=2)
    private BigDecimal monto;
    @Column(name="descripcion", nullable=false, length=200)
    private String descripcion;
    @Column(name="fecha_hora", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaHora;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_concepto", nullable=false, unique=false)
    private ConceptoMovimiento concepto;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_usuario", nullable=false, unique=false)
    private Usuario usuario;
    @OneToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_compra", nullable=true, unique=true)
    private Compra compra;
    @OneToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_venta", nullable=true, unique=true)
    private Venta venta;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_caja", nullable=false, unique=false)
    private CajaDiaria caja;

    public MovimientoCaja() { }
    public MovimientoCaja(String id, String idConcepto, Long idUsuario, String idVenta, String idCompra, String tipo, BigDecimal monto, String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.idConcepto = idConcepto;
        this.idUsuario = idUsuario;
        this.idVenta = idVenta;
        this.idCompra = idCompra;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }
    public Long getClave() { return clave; }
    public void setClave(Long valor) { this.clave = valor; }
    public String getId() { return id; }
    public void setId(String valor) { this.id = valor; }
    public String getIdConcepto() { return idConcepto != null ? idConcepto : concepto == null ? null : concepto.getId(); }
    public void setIdConcepto(String valor) { this.idConcepto = valor; }
    public Long getIdUsuario() { return idUsuario != null ? idUsuario : usuario == null ? null : usuario.getId(); }
    public void setIdUsuario(Long valor) { this.idUsuario = valor; }
    public String getIdVenta() { return idVenta != null ? idVenta : venta == null ? null : venta.getId(); }
    public void setIdVenta(String valor) { this.idVenta = valor; }
    public String getIdCompra() { return idCompra != null ? idCompra : compra == null ? null : compra.getId(); }
    public void setIdCompra(String valor) { this.idCompra = valor; }
    public String getTipo() { return tipo; }
    public void setTipo(String valor) { this.tipo = valor; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal valor) { this.monto = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String valor) { this.descripcion = valor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime valor) { this.fechaHora = valor; }
    public ConceptoMovimiento getConcepto() { return concepto; }
    public void setConcepto(ConceptoMovimiento valor) { this.concepto = valor; this.idConcepto = valor == null ? null : valor.getId(); }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario valor) { this.usuario = valor; this.idUsuario = valor == null ? null : valor.getId(); }
    public Compra getCompra() { return compra; }
    public void setCompra(Compra valor) { this.compra = valor; this.idCompra = valor == null ? null : valor.getId(); }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta valor) { this.venta = valor; this.idVenta = valor == null ? null : valor.getId(); }
    public CajaDiaria getCaja() { return caja; }
    public void setCaja(CajaDiaria valor) { this.caja = valor; }
}
