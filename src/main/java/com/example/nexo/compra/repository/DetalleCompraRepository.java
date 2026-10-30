package com.example.nexo.compra.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.compra.model.DetalleCompra;
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra,Long> {
    @Query("select d from DetalleCompra d where d.compra.id=:codigo order by d.id")
    List<DetalleCompra> porOperacion(@Param("codigo") String codigo);
}
