package com.example.nexo.asistencia.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.asistencia.model.Asistencia;
public interface AsistenciaRepository extends JpaRepository<Asistencia,Long> {
    List<Asistencia> findByEmpleadoIdOrderByFechaDesc(Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Asistencia> findByEmpleadoIdAndFecha(Long id,java.time.LocalDate fecha);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Asistencia> findFirstByEmpleadoIdAndHoraSalidaIsNullOrderByFechaDesc(Long id);
}
