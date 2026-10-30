package com.example.nexo.asistencia.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;
import com.example.nexo.empleado.model.Empleado;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="asistencia", uniqueConstraints={@UniqueConstraint(columnNames={"id_empleado","fecha"})})
@org.hibernate.annotations.DynamicUpdate
public class Asistencia {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_asistencia", nullable=false)
    private Long id;
    @Transient
    private Long idEmpleado;
    @Column(name="fecha", nullable=false)
    private LocalDate fecha;
    @Column(name="hora_entrada", nullable=false, columnDefinition="TIME")
    private LocalTime horaEntrada;
    @Column(name="hora_salida", nullable=true, columnDefinition="TIME")
    private LocalTime horaSalida;
    @Column(name="estado", nullable=false, length=10)
    @Convert(converter=TipoMovimientoConverter.class)
    private String estado;
    @Column(name="observacion", nullable=true, length=200)
    private String observacion;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empleado", nullable=false, unique=false)
    private Empleado empleado;

    public Asistencia() { }
    public Asistencia(Long id, Long idEmpleado, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, String estado, String observacion) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
        this.observacion = observacion;
    }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public Long getIdEmpleado() { return idEmpleado != null ? idEmpleado : empleado == null ? null : empleado.getId(); }
    public void setIdEmpleado(Long valor) { this.idEmpleado = valor; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate valor) { this.fecha = valor; }
    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime valor) { this.horaEntrada = valor; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime valor) { this.horaSalida = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String valor) { this.observacion = valor; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado valor) { this.empleado = valor; this.idEmpleado = valor == null ? null : valor.getId(); }
}
