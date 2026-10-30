package com.example.nexo.compra.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.compra.model.Compra;
public interface CompraRepository extends JpaRepository<Compra,Long> {
    @Query("select e from Compra e where e.id=:codigo")
    Optional<Compra> buscarCodigo(@Param("codigo") String codigo);
}
