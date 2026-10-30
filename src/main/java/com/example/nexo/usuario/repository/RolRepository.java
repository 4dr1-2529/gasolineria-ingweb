package com.example.nexo.usuario.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.usuario.model.Rol;
public interface RolRepository extends JpaRepository<Rol,Long> {
    Optional<Rol> findByNombre(String nombre);
    Optional<Rol> findByCodigo(String codigo);
}
