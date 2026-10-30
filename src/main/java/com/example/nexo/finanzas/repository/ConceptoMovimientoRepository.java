package com.example.nexo.finanzas.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.finanzas.model.ConceptoMovimiento;
public interface ConceptoMovimientoRepository extends JpaRepository<ConceptoMovimiento,Long> {
    @Query("select e from ConceptoMovimiento e where e.id=:codigo")
    Optional<ConceptoMovimiento> buscarCodigo(@Param("codigo") String codigo);
}
