package com.example.nexo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Empleado;

/**
 * Guarda los empleados en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son los tres empleados de la versión 1.
 *
 * Validaciones de servidor (F29 y F31): DNI obligatorio de 8 dígitos y único,
 * nombres y apellidos de 3 a 60, cargo de 3 a 40, teléfono con el formato
 * 000 000 000 y estado del catálogo (Activo / Inactivo).
 * RN02: nunca se elimina un empleado, sólo se cambia su estado (F31).
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

    public Empleado buscarEmpleadoPorId(Integer id) {
        if (id == null) {
            return null;
        }
        for (Empleado empleado : empleados) {
            if (id.equals(empleado.getId())) {
                return empleado;
            }
        }
        // Id inexistente: se devuelve null y el controller regresa a la lista
        return null;
    }

    public String crearEmpleado(Empleado empleado) {
        String error = validar(empleado, null);
        if (error != null) {
            return error;
        }
        normalizar(empleado);
        empleado.setId(siguienteId());
        empleados.add(empleado);
        return null; // alta completada
    }

    public String editarEmpleado(Empleado empleado) {
        Empleado existente = buscarEmpleadoPorId(empleado.getId());
        if (existente == null) {
            return "No se encontró el empleado solicitado.";
        }
        String error = validar(empleado, empleado.getId());
        if (error != null) {
            return error;
        }
        normalizar(empleado);
        existente.setDni(empleado.getDni());
        existente.setNombres(empleado.getNombres());
        existente.setApellidos(empleado.getApellidos());
        existente.setCargo(empleado.getCargo());
        existente.setTelefono(empleado.getTelefono());
        existente.setEstado(empleado.getEstado());
        return null; // edición completada: se conserva el id y el historial
    }

    public String cambiarEstadoEmpleado(Integer id, String estado) {
        Empleado empleado = buscarEmpleadoPorId(id);
        if (empleado == null) {
            return "No se encontró el empleado solicitado.";
        }
        if (!esEstadoValido(estado)) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // RN02: el registro se conserva con su nuevo estado; no se elimina
        empleado.setEstado(estado);
        return null;
    }

    /**
     * Reglas comunes de alta y edición. 'idExcluido' es el id que no compite
     * consigo mismo al validar la unicidad del DNI (edición).
     */
    private String validar(Empleado empleado, Integer idExcluido) {
        if (empleado == null) {
            return "Los datos del empleado son obligatorios.";
        }
        String dni = texto(empleado.getDni());
        if (!dni.matches("[0-9]{8}")) {
            return "El DNI debe tener exactamente 8 dígitos.";
        }
        // Unicidad del DNI: una misma persona no se registra dos veces
        for (Empleado existente : empleados) {
            boolean mismo = idExcluido != null && idExcluido.equals(existente.getId());
            if (!mismo && dni.equals(texto(existente.getDni()))) {
                return "Ya existe un empleado con el DNI " + dni + ".";
            }
        }
        if (!longitudEntre(empleado.getNombres(), 3, 60)) {
            return "Los nombres deben tener entre 3 y 60 caracteres.";
        }
        if (!longitudEntre(empleado.getApellidos(), 3, 60)) {
            return "Los apellidos deben tener entre 3 y 60 caracteres.";
        }
        if (!longitudEntre(empleado.getCargo(), 3, 40)) {
            return "El cargo debe tener entre 3 y 40 caracteres.";
        }
        String telefono = texto(empleado.getTelefono());
        if (!telefono.matches("[0-9]{3} [0-9]{3} [0-9]{3}")) {
            return "El teléfono debe seguir el formato 000 000 000.";
        }
        if (!esEstadoValido(empleado.getEstado())) {
            return "El estado debe ser Activo o Inactivo.";
        }
        return null; // sin errores
    }

    private boolean esEstadoValido(String estado) {
        return "Activo".equals(estado) || "Inactivo".equals(estado);
    }

    private boolean longitudEntre(String valor, int minimo, int maximo) {
        String texto = texto(valor);
        return texto.length() >= minimo && texto.length() <= maximo;
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    /** Guarda los campos sin los espacios sobrantes. */
    private void normalizar(Empleado empleado) {
        empleado.setDni(texto(empleado.getDni()));
        empleado.setNombres(texto(empleado.getNombres()));
        empleado.setApellidos(texto(empleado.getApellidos()));
        empleado.setCargo(texto(empleado.getCargo()));
        empleado.setTelefono(texto(empleado.getTelefono()));
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
