package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Empleado;

public interface EmpleadoService {

    public List<Empleado> listaEmpleados();

    public void crearEmpleado(Empleado empleado);

    public Empleado buscarEmpleadoPorId(Integer id);

    public void editarEmpleado(Empleado empleado);

}
