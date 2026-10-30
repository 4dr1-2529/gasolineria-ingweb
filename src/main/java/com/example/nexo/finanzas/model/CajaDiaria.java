package com.example.nexo.finanzas.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.usuario.model.Usuario;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="caja_diaria", uniqueConstraints={@UniqueConstraint(columnNames={"fecha"})})
@org.hibernate.annotations.DynamicUpdate
public class CajaDiaria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_caja", nullable=false)
    private Long id;
    @Column(name="fecha", nullable=false)
    private LocalDate fecha;
    @Column(name="saldo_apertura", nullable=false, precision=12, scale=2)
    private BigDecimal saldoApertura;
    @Column(name="saldo_cierre", nullable=true, precision=12, scale=2)
    private BigDecimal saldoCierre;
    @Column(name="estado", nullable=false, length=10)
    private String estado;
    @Column(name="fecha_apertura", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaApertura;
    @Column(name="fecha_cierre", nullable=true, columnDefinition="DATETIME")
    private LocalDateTime fechaCierre;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_usuario_apertura", nullable=false, unique=false)
    private Usuario usuarioApertura;
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_usuario_cierre", nullable=true, unique=false)
    private Usuario usuarioCierre;

    public CajaDiaria() { }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate valor) { this.fecha = valor; }
    public BigDecimal getSaldoApertura() { return saldoApertura; }
    public void setSaldoApertura(BigDecimal valor) { this.saldoApertura = valor; }
    public BigDecimal getSaldoCierre() { return saldoCierre; }
    public void setSaldoCierre(BigDecimal valor) { this.saldoCierre = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime valor) { this.fechaApertura = valor; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime valor) { this.fechaCierre = valor; }
    public Usuario getUsuarioApertura() { return usuarioApertura; }
    public void setUsuarioApertura(Usuario valor) { this.usuarioApertura = valor; }
    public Usuario getUsuarioCierre() { return usuarioCierre; }
    public void setUsuarioCierre(Usuario valor) { this.usuarioCierre = valor; }
}
