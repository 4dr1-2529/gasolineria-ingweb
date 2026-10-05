package com.example.nexo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Empleado;

/**
 * Guarda los empleados en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son los tres empleados de la versión 1.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final List<Empleado> empleados = new ArrayList<>();

    public EmpleadoServiceImpl() {
        // Datos de partida tomados de la versión 1 (lista de personal)
        empleados.add(new Empleado(1, "00000001", "Ana", "Torres", "Operadora", "000 000 001", "Activo"));
        empleados.add(new Empleado(2, "00000002", "Luis", "Rojas", "Vendedor", "000 000 002", "Activo"));
        empleados.add(new Empleado(3, "00000003", "Elena", "Díaz", "Administradora", "000 000 003", "Activo"));
    }

    public List<Empleado> listaEmpleados() {
        return empleados;
    }

    public void crearEmpleado(Empleado empleado) {
        empleado.setId(siguienteId());
        empleados.add(empleado);
    }

    public Empleado buscarEmpleadoPorId(Integer id) {
        if (id == null) {
            return null;
        }
        for (Empleado empleado : empleados) {
            if (empleado.getId().equals(id)) {
                return empleado;
            }
        }
        // Id inexistente: se devuelve null y el controller regresa a la lista
        return null;
    }

    public void editarEmpleado(Empleado empleado) {
        if (empleado.getId() == null) {
            return;
        }
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId().equals(empleado.getId())) {
                empleados.set(i, empleado);
                return;
            }
        }
        // Id inexistente: no se modifica nada
    }

    /**
     * El id_empleado es entero en el modelo de la versión 1,
     * así que se conserva la numeración correlativa 1, 2, 3 → 4.
     */
    private Integer siguienteId() {
        int maximo = 0;
        for (Empleado empleado : empleados) {
            if (empleado.getId() != null && empleado.getId() > maximo) {
                maximo = empleado.getId();
            }
        }
        return maximo + 1;
    }

}
