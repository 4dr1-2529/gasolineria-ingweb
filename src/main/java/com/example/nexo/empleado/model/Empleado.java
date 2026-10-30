package com.example.nexo.empleado.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="empleado", uniqueConstraints={@UniqueConstraint(columnNames={"dni"})})
@org.hibernate.annotations.DynamicUpdate
public class Empleado {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_empleado", nullable=false)
    private Long id;
    @Column(name="dni", nullable=false, length=8, columnDefinition="CHAR(8)")
    private String dni;
    @Column(name="nombres", nullable=false, length=60)
    private String nombres;
    @Column(name="apellidos", nullable=false, length=60)
    private String apellidos;
    @Column(name="cargo", nullable=false, length=40)
    private String cargo;
    @Column(name="telefono", nullable=false, length=15)
    private String telefono;
    @Column(name="estado", nullable=false, length=10)
    private String estado;
    @Column(name="fecha_ingreso", nullable=true)
    private LocalDate fechaIngreso;

    public Empleado() { }
    public Empleado(Long id, String dni, String nombres, String apellidos, String cargo, String telefono, String estado) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.telefono = telefono;
        this.estado = estado;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getDni() { return dni; }
    public void setDni(String valor) { this.dni = valor; }
    public String getNombres() { return nombres; }
    public void setNombres(String valor) { this.nombres = valor; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String valor) { this.apellidos = valor; }
    public String getCargo() { return cargo; }
    public void setCargo(String valor) { this.cargo = valor; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String valor) { this.telefono = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate valor) { this.fechaIngreso = valor; }
}
