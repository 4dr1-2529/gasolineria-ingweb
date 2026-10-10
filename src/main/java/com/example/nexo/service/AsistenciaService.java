package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Asistencia;
import com.example.nexo.model.Empleado;

/**
 * F35–F38 · Asistencia del personal (P28 y P29).
 * La identidad del empleado nunca llega del navegador: el controller resuelve
 * al empleado dueño de la sesión autenticada y lo pasa aquí (RN05).
 */
public interface AsistenciaService {

    /** P29 · todas las marcaciones del periodo. */
    public List<Asistencia> listaAsistencias();

    /** RN05 · las marcaciones de un solo empleado (P28 usa el de la sesión). */
    public List<Asistencia> listaAsistenciasDeEmpleado(Integer idEmpleado);

    /** RN05 · la asistencia abierta (entrada sin salida) de hoy, o null. */
    public Asistencia buscarAsistenciaAbierta(Integer idEmpleado);

    /** RN05 · registra la entrada de hoy para ese empleado. */
    public String registrarEntrada(Integer idEmpleado);

    /** RN05 · cierra la asistencia abierta de ese empleado. */
    public String registrarSalida(Integer idEmpleado);

    /**
     * Empleado dueño de la sesión, para mostrar su nombre en P28.
     * Devuelve null si la cuenta no tiene empleado asociado.
     */
    public Empleado buscarEmpleado(Integer idEmpleado);

}
