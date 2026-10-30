package com.example.nexo.asistencia.service;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.nexo.asistencia.model.Asistencia;
import com.example.nexo.asistencia.repository.AsistenciaRepository;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.repository.EmpleadoRepository;
import com.example.nexo.auth.service.ActorActual;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
import com.example.nexo.shared.config.TiempoConfig;
@Service @Transactional
public class AsistenciaServiceImpl implements AsistenciaService {
 private final AsistenciaRepository repo; private final EmpleadoRepository empleados; private final ActorActual actor; private final Clock reloj;
 public AsistenciaServiceImpl(AsistenciaRepository repo,EmpleadoRepository empleados,ActorActual actor,Clock reloj) {this.repo=repo;this.empleados=empleados;this.actor=actor;this.reloj=reloj.withZone(TiempoConfig.LIMA);}
 @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
 public List<Asistencia> listaAsistencias() {return repo.findAll(org.springframework.data.domain.Sort.by("fecha").descending());}
 public List<Asistencia> listaAsistenciasDeEmpleado(Long id) {return id==null?List.of():repo.findByEmpleadoIdOrderByFechaDesc(id);}
 public Asistencia buscarAsistenciaAbierta(Long id) {return id==null?null:repo.findFirstByEmpleadoIdAndHoraSalidaIsNullOrderByFechaDesc(id).orElse(null);}
 public Empleado buscarEmpleado(Long id) {return id==null?null:empleados.findById(id).orElse(null);}
 private Empleado propio(Long id) {exigir(actor.usuario().getIdEmpleado().equals(id),"Solo puedes registrar tu propia asistencia.");Empleado e=empleados.bloquear(id).orElse(null);exigir(e!=null && "Activo".equals(e.getEstado()),"Sólo los empleados activos pueden registrar asistencia.");return e;}
 @PreAuthorize("isAuthenticated()")
 public String registrarEntrada(Long id) {
  Empleado empleado=propio(id);if(buscarAsistenciaAbierta(id)!=null)return "Ya tienes una asistencia abierta: registra la salida antes de iniciar otra jornada.";
  LocalDateTime ahora=LocalDateTime.now(reloj).withNano(0);if(repo.findByEmpleadoIdAndFecha(id,ahora.toLocalDate()).isPresent())return "Ya existe tu asistencia de hoy: sólo puede registrarse una por día.";
  Asistencia a=new Asistencia();a.setEmpleado(empleado);a.setFecha(ahora.toLocalDate());a.setHoraEntrada(ahora.toLocalTime());a.setEstado("Presente");repo.saveAndFlush(a);return "Entrada registrada a las "+ahora.toLocalTime().withNano(0)+".";
 }
 @PreAuthorize("isAuthenticated()")
 public String registrarSalida(Long id) {
  propio(id);Asistencia a=buscarAsistenciaAbierta(id);if(a==null)return "No hay una asistencia abierta para registrar la salida.";
  LocalDateTime ahora=LocalDateTime.now(reloj).withNano(0);if(!ahora.toLocalDate().equals(a.getFecha()))return "La jornada abierta pertenece a otra fecha: solicita su revisión al Administrador.";
  if(!ahora.toLocalTime().isAfter(a.getHoraEntrada()))return "La hora de salida debe ser posterior a la hora de entrada.";
  a.setHoraSalida(ahora.toLocalTime());a.setEstado("Presente");repo.flush();return "Salida registrada a las "+a.getHoraSalida().withNano(0)+".";
 }
}
