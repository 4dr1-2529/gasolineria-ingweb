package com.example.nexo.finanzas.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.finanzas.model.CajaDiaria;
public interface CajaDiariaRepository extends JpaRepository<CajaDiaria,Long> {
    Optional<CajaDiaria> findByFecha(java.time.LocalDate fecha);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select c from CajaDiaria c where c.fecha=:fecha")
    Optional<CajaDiaria> bloquear(@Param("fecha") java.time.LocalDate fecha);
}
