package com.example.nexo.finanzas.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.finanzas.model.MovimientoCaja;
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja,Long> {
    @Query("select e from MovimientoCaja e where e.id=:codigo")
    Optional<MovimientoCaja> buscarCodigo(@Param("codigo") String codigo);
}
