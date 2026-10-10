package com.example.nexo.usuario.model;

public class Usuario {

    private Integer id;
    private Integer idEmpleado;
    private String username;
    private String password;
    private String rol;
    private String estado;

    public Usuario() {
        // Constructor vacío
    }

    public Usuario(Integer id, Integer idEmpleado, String username, String password,
                   String rol, String estado) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
