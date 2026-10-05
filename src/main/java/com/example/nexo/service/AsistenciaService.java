package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Asistencia;
import com.example.nexo.model.Empleado;

public interface AsistenciaService {

    public List<Asistencia> listaAsistencias();

    public List<Asistencia> listaAsistenciasEmpleadoActual();

    public List<Asistencia> listaAsistenciasPorEmpleado(Integer idEmpleado);

    public Asistencia buscarAsistenciaAbierta();

    public String registrarEntrada();

    public String registrarSalida();

    /**
     * TEMPORAL: devuelve el empleado de demostración mientras no existe
     * autenticación. Con Spring Security se reemplazará por el usuario autenticado.
     */
    public Empleado getEmpleadoActual();

}
