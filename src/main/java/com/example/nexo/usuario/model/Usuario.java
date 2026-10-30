package com.example.nexo.usuario.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.usuario.model.Rol;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="usuario", uniqueConstraints={@UniqueConstraint(columnNames={"username"}),@UniqueConstraint(columnNames={"id_empleado"})})
@org.hibernate.annotations.DynamicUpdate
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_usuario", nullable=false)
    private Long id;
    @Transient
    private Long idEmpleado;
    @Column(name="username", nullable=false, length=30)
    private String username;
    @Column(name="password_hash", nullable=false, length=100)
    private String password;
    @Transient
    private String rol;
    @Column(name="estado", nullable=false, length=10)
    private String estado;
    @Column(name="fecha_creacion", nullable=false, columnDefinition="DATETIME")
    private LocalDateTime fechaCreacion;
    @Column(name="ultimo_acceso", nullable=true, columnDefinition="DATETIME")
    private LocalDateTime ultimoAcceso;
    @OneToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empleado", nullable=false, unique=true)
    private Empleado empleado;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_rol", nullable=false, unique=false)
    private Rol perfil;

    public Usuario() { }
    public Usuario(Long id, Long idEmpleado, String username, String password, String rol, String estado) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public Long getIdEmpleado() { return idEmpleado != null ? idEmpleado : empleado == null ? null : empleado.getId(); }
    public void setIdEmpleado(Long valor) { this.idEmpleado = valor; }
    public String getUsername() { return username; }
    public void setUsername(String valor) { this.username = valor; }
    public String getPassword() { return password; }
    public void setPassword(String valor) { this.password = valor; }
    public String getRol() { return rol != null ? rol : perfil == null ? null : perfil.getNombre(); }
    public void setRol(String valor) { this.rol = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime valor) { this.fechaCreacion = valor; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime valor) { this.ultimoAcceso = valor; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado valor) { this.empleado = valor; this.idEmpleado = valor == null ? null : valor.getId(); }
    public Rol getPerfil() { return perfil; }
    public void setPerfil(Rol valor) { this.perfil = valor; this.rol = valor == null ? null : valor.getNombre(); }
    @PrePersist void fechaInicial() { if (fechaCreacion == null) fechaCreacion = LocalDateTime.now(com.example.nexo.shared.config.TiempoConfig.LIMA); }
}
