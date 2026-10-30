package com.example.nexo.inventario.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.inventario.model.MovimientoInventario;
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario,Long> {
}
