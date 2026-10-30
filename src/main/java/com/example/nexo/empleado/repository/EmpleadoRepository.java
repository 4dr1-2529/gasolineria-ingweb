package com.example.nexo.empleado.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.empleado.model.Empleado;
public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select e from Empleado e where e.id=:id")
    Optional<Empleado> bloquear(@Param("id") Long id);
}
