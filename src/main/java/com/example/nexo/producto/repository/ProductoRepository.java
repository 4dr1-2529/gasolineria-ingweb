package com.example.nexo.producto.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.producto.model.Producto;
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    @Query("select e from Producto e where e.id=:codigo")
    Optional<Producto> buscarCodigo(@Param("codigo") String codigo);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Producto p where p.id=:codigo")
    Optional<Producto> bloquear(@Param("codigo") String codigo);
}
