package com.example.nexo.venta.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.usuario.model.Usuario;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="venta", uniqueConstraints={@UniqueConstraint(columnNames={"codigo_venta"})})
@org.hibernate.annotations.DynamicUpdate
public class Venta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_venta", nullable=false)
    private Long clave;
    @Column(name="codigo_venta", nullable=false, length=12)
    private String id;
    @Transient
    private Long idUsuario;
    @Column(name="fecha_hora", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaHora;
    @Column(name="total", nullable=false, precision=12, scale=2)
    private BigDecimal total;
    @Column(name="estado", nullable=false, length=20)
    private String estado;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_usuario", nullable=false, unique=false)
    private Usuario usuario;

    public Venta() { }
    public Venta(String id, Long idUsuario, LocalDateTime fechaHora, BigDecimal total, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaHora = fechaHora;
        this.total = total;
        this.estado = estado;
    }
    public Long getClave() { return clave; }
    public void setClave(Long valor) { this.clave = valor; }
    public String getId() { return id; }
    public void setId(String valor) { this.id = valor; }
    public Long getIdUsuario() { return idUsuario != null ? idUsuario : usuario == null ? null : usuario.getId(); }
    public void setIdUsuario(Long valor) { this.idUsuario = valor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime valor) { this.fechaHora = valor; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal valor) { this.total = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario valor) { this.usuario = valor; this.idUsuario = valor == null ? null : valor.getId(); }
}
