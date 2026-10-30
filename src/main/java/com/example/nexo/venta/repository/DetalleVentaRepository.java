package com.example.nexo.venta.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.venta.model.DetalleVenta;
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Long> {
    @Query("select d from DetalleVenta d where d.venta.id=:codigo order by d.id")
    List<DetalleVenta> porOperacion(@Param("codigo") String codigo);
}
