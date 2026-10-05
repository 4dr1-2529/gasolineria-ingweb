package com.example.nexo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Asistencia;
import com.example.nexo.model.Empleado;

/**
 * Guarda las asistencias (marcaciones del personal) en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son las marcaciones documentadas de la versión 1.
 *
 * Se aplican aquí las reglas de asistencia de la versión 1:
 * RN05 cada quien registra y consulta su propia asistencia (no hay selector),
 * una sola asistencia por empleado y jornada,
 * la hora de salida debe ser posterior a la entrada,
 * el estado se calcula en servidor a partir de las horas.
 */
@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    /**
     * TEMPORAL: mientras no existe Spring Security, la aplicación trabaja con un
     * empleado de demostración (Ana Torres). Cuando exista autenticación, este
     * valor será reemplazado por el empleado del usuario autenticado.
     */
    private static final Integer EMPLEADO_ACTUAL_DEMO = 1;

    private final List<Asistencia> asistencias = new ArrayList<>();
    private final EmpleadoService empleadoService;

    public AsistenciaServiceImpl(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
        // Datos de partida tomados de la versión 1 (asistencia del periodo)
        asistencias.add(crear(1, 1, LocalDate.of(2026, 9, 10), LocalTime.of(8, 0), LocalTime.of(17, 0), "Jornada completa"));
        asistencias.add(crear(2, 1, LocalDate.of(2026, 9, 9), LocalTime.of(7, 58), LocalTime.of(17, 0), "Jornada completa"));
        asistencias.add(crear(3, 1, LocalDate.of(2026, 9, 8), null, null, "Sin marcación del día"));
        asistencias.add(crear(4, 2, LocalDate.of(2026, 9, 10), LocalTime.of(8, 0), LocalTime.of(17, 0), "Jornada completa"));
        asistencias.add(crear(5, 3, LocalDate.of(2026, 9, 10), LocalTime.of(8, 15), LocalTime.of(17, 0), "Ingreso posterior a las 08:00"));
        ordenar();
    }

    public List<Asistencia> listaAsistencias() {
        return asistencias;
    }

    public List<Asistencia> listaAsistenciasEmpleadoActual() {
        List<Asistencia> propias = new ArrayList<>();
        for (Asistencia asistencia : asistencias) {
            // RN05: sólo la asistencia del empleado actual (no hay selector)
            if (asistencia.getIdEmpleado().equals(EMPLEADO_ACTUAL_DEMO)) {
                propias.add(asistencia);
            }
        }
        return propias;
    }

    public List<Asistencia> listaAsistenciasPorEmpleado(Integer idEmpleado) {
        if (idEmpleado == null) {
            return asistencias;
        }
        List<Asistencia> filtradas = new ArrayList<>();
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getIdEmpleado().equals(idEmpleado)) {
                filtradas.add(asistencia);
            }
        }
        return filtradas;
    }

    /**
     * Devuelve la asistencia abierta de la jornada de hoy (entrada sin salida)
     * o null si no existe.
     */
    public Asistencia buscarAsistenciaAbierta() {
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getIdEmpleado().equals(EMPLEADO_ACTUAL_DEMO)
                    && asistencia.getFecha().equals(LocalDate.now())
                    && asistencia.getHoraEntrada() != null
                    && asistencia.getHoraSalida() == null) {
                return asistencia;
            }
        }
        return null;
    }

    public String registrarEntrada() {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(EMPLEADO_ACTUAL_DEMO);
        if (empleado == null) {
            return "No se encontró el empleado de la demostración.";
        }
        if (!"Activo".equals(empleado.getEstado())) {
            // Sólo los empleados activos pueden registrar asistencia
            return "Sólo los empleados activos pueden registrar asistencia.";
        }
        if (buscarAsistenciaAbierta() != null) {
            // RN05: no puede haber una segunda asistencia abierta
            return "Ya registraste la entrada de hoy: no puede haber una segunda asistencia abierta.";
        }
        LocalDate hoy = LocalDate.now();
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getIdEmpleado().equals(empleado.getId()) && asistencia.getFecha().equals(hoy)) {
                // RN05: una sola asistencia por empleado y día
                return "Ya existe tu asistencia de hoy: sólo puede registrarse una por día.";
            }
        }
        LocalTime horaEntrada = LocalTime.now();
        asistencias.add(new Asistencia(siguienteId(), empleado.getId(), hoy, horaEntrada, null,
                estadoPorHoras(horaEntrada, null), null));
        ordenar();
        return "Entrada registrada a las " + hora(horaEntrada) + ".";
    }

    public String registrarSalida() {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(EMPLEADO_ACTUAL_DEMO);
        if (empleado == null) {
            return "No se encontró el empleado de la demostración.";
        }
        Asistencia abierta = buscarAsistenciaAbierta();
        if (abierta == null) {
            // No hay asistencia abierta: no se lanza ningún error
            return "No hay una asistencia abierta para registrar la salida.";
        }
        LocalTime horaSalida = LocalTime.now();
        if (!horaSalida.isAfter(abierta.getHoraEntrada())) {
            // RN05: la salida debe ser posterior a la entrada
            return "La hora de salida debe ser posterior a la hora de entrada.";
        }
        abierta.setHoraSalida(horaSalida);
        abierta.setEstado(estadoPorHoras(abierta.getHoraEntrada(), horaSalida)); // RN05
        return "Salida registrada a las " + hora(horaSalida) + ".";
    }

    public Empleado getEmpleadoActual() {
        return empleadoService.buscarEmpleadoPorId(EMPLEADO_ACTUAL_DEMO);
    }

    /**
     * Crea una marcación calculando el estado a partir de las horas (RN05):
     * sin ninguna hora es Falta; con horas registradas es Presente.
     * El estado nunca se digita.
     */
    private Asistencia crear(Integer id, Integer idEmpleado, LocalDate fecha, LocalTime horaEntrada,
                             LocalTime horaSalida, String observacion) {
        return new Asistencia(id, idEmpleado, fecha, horaEntrada, horaSalida,
                estadoPorHoras(horaEntrada, horaSalida), observacion);
    }

    private String estadoPorHoras(LocalTime horaEntrada, LocalTime horaSalida) {
        if (horaEntrada == null && horaSalida == null) {
            return "Falta"; // día sin marcación
        }
        return "Presente"; // jornada con horas registradas
    }

    /**
     * Mantiene las marcaciones de la más reciente a la más antigua.
     */
    private void ordenar() {
        asistencias.sort(Comparator.comparing(Asistencia::getFecha).reversed());
    }

    private String hora(LocalTime hora) {
        return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
    }

    /**
     * El id_asistencia es entero en el modelo de la versión 1,
     * así que se conserva la numeración correlativa 1, 2, 3 … 5 → 6.
     */
    private Integer siguienteId() {
        int maximo = 0;
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getId() != null && asistencia.getId() > maximo) {
                maximo = asistencia.getId();
            }
        }
        return maximo + 1;
    }

}
