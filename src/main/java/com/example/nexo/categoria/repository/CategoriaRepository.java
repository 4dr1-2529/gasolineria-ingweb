package com.example.nexo.categoria.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.example.nexo.categoria.model.Categoria;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
}
