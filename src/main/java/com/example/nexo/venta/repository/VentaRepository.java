package com.example.nexo.venta.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.venta.model.Venta;
public interface VentaRepository extends JpaRepository<Venta,Long> {
    @Query("select e from Venta e where e.id=:codigo")
    Optional<Venta> buscarCodigo(@Param("codigo") String codigo);
}
