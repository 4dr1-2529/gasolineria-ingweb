package com.example.nexo.proveedor.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.proveedor.model.Proveedor;
public interface ProveedorRepository extends JpaRepository<Proveedor,Long> {
}
