package com.example.nexo.empleado.service;

import java.util.List;
import com.example.nexo.empleado.model.Empleado;


/**
 * Personal de la estación (F29 registrar, F30 consultar, F31 editar y
 * cambiar estado).
 * Cada método de escritura devuelve un mensaje de error en español, o
 * null si la operación se completó: el controller lo convierte en aviso.
 */
public interface EmpleadoService {

    public List<Empleado> listaEmpleados();

    /** Busca por identificador; devuelve null si no existe. */
    public Empleado buscarEmpleadoPorId(Integer id);

    /** F29 · alta de un empleado; devuelve el error o null si se registró. */
    public String crearEmpleado(Empleado empleado);

    /** F31 · edición conservando el id; devuelve el error o null. */
    public String editarEmpleado(Empleado empleado);

    /** F31 · RN02 · cambia el estado sin eliminar el registro. */
    public String cambiarEstadoEmpleado(Integer id, String estado);

}
