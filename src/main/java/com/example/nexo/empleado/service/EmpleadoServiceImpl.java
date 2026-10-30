package com.example.nexo.empleado.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.empleado.model.Empleado;


/** Servicio persistente: conserva validación, baja lógica e historial. */
@Service
@org.springframework.transaction.annotation.Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final com.example.nexo.empleado.repository.EmpleadoRepository repo;



    public EmpleadoServiceImpl(com.example.nexo.empleado.repository.EmpleadoRepository repo) {
        this.repo=repo;

    }

    public List<Empleado> listaEmpleados() {
        return repo.findAll();
    }

    public Empleado buscarEmpleadoPorId(Long id) {return id==null?null:repo.findById(id).orElse(null);}

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String crearEmpleado(Empleado empleado) {
        String error = validar(empleado, null);
        if (error != null) {
            return error;
        }
        normalizar(empleado);
        empleado.setId(null);
        repo.save(empleado);
        return null; // alta completada
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
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

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String cambiarEstadoEmpleado(Long id, String estado) {
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
    private String validar(Empleado empleado, Long idExcluido) {
        if (empleado == null) {
            return "Los datos del empleado son obligatorios.";
        }
        String dni = texto(empleado.getDni());
        if (!dni.matches("[0-9]{8}")) {
            return "El DNI debe tener exactamente 8 dígitos.";
        }
        // Unicidad del DNI: una misma persona no se registra dos veces
        for (Empleado existente : repo.findAll()) {
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


    public List<Empleado> filtrar(String dni,String nombre,String cargo,String estado) {
        return repo.findAll().stream().filter(e->e.getDni().contains(com.example.nexo.shared.validation.TextoNormalizado.nombre(dni)))
            .filter(e->com.example.nexo.shared.validation.TextoNormalizado.clave(e.getNombres()+" "+e.getApellidos()).contains(com.example.nexo.shared.validation.TextoNormalizado.clave(nombre)))
            .filter(e->com.example.nexo.shared.validation.TextoNormalizado.clave(e.getCargo()).contains(com.example.nexo.shared.validation.TextoNormalizado.clave(cargo)))
            .filter(e->estado==null || estado.isBlank() || estado.equals(e.getEstado())).toList();
    }
}
