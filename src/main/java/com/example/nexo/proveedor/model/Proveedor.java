package com.example.nexo.proveedor.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import com.example.nexo.shared.persistence.TipoMovimientoConverter;

/** Entidad del diccionario aprobado; los identificadores de formulario son transitorios. */
@Entity
@Table(name="proveedor", uniqueConstraints={@UniqueConstraint(columnNames={"ruc"})})
@org.hibernate.annotations.DynamicUpdate
public class Proveedor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_proveedor", nullable=false)
    private Long id;
    @Column(name="ruc", nullable=false, length=11, columnDefinition="CHAR(11)")
    private String ruc;
    @Column(name="razon_social", nullable=false, length=120)
    private String razonSocial;
    @Column(name="contacto", nullable=true, length=80)
    private String contacto;
    @Column(name="telefono", nullable=true, length=15)
    private String telefono;
    @Column(name="correo", nullable=true, length=120)
    private String correo;
    @Column(name="direccion", nullable=true, length=200)
    private String direccion;
    @Column(name="estado", nullable=false, length=10)
    private String estado;

    public Proveedor() { }
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getRuc() { return ruc; }
    public void setRuc(String valor) { this.ruc = valor; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String valor) { this.razonSocial = valor; }
    public String getContacto() { return contacto; }
    public void setContacto(String valor) { this.contacto = valor; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String valor) { this.telefono = valor; }
    public String getCorreo() { return correo; }
    public void setCorreo(String valor) { this.correo = valor; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String valor) { this.direccion = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String valor) { this.estado = valor; }
}
