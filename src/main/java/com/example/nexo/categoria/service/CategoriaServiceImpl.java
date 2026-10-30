package com.example.nexo.categoria.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.categoria.model.Categoria;


/** Servicio persistente: conserva validación, baja lógica e historial. */
@Service
@org.springframework.transaction.annotation.Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final com.example.nexo.categoria.repository.CategoriaRepository repo;



    public CategoriaServiceImpl(com.example.nexo.categoria.repository.CategoriaRepository repo) {
        this.repo=repo;

    }

    public List<Categoria> listaCategorias() {
        return repo.findAll();
    }

    public Categoria buscarCategoriaPorId(Long id) {return id==null?null:repo.findById(id).orElse(null);}

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String crearCategoria(Categoria categoria) {
        String error = validar(categoria, null);
        if (error != null) {
            return error;
        }
        normalizar(categoria);
        categoria.setId(null);
        repo.save(categoria);
        return null; // alta completada
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String editarCategoria(Categoria categoria) {
        Categoria existente = buscarCategoriaPorId(categoria.getId());
        if (existente == null) {
            return "No se encontró la categoría solicitada.";
        }
        String error = validar(categoria, categoria.getId());
        if (error != null) {
            return error;
        }
        normalizar(categoria);
        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setEstado(categoria.getEstado());
        return null; // edición completada: se conserva el id y el historial
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String cambiarEstadoCategoria(Long id, String estado) {
        Categoria categoria = buscarCategoriaPorId(id);
        if (categoria == null) {
            return "No se encontró la categoría solicitada.";
        }
        if (!esEstadoValido(estado)) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // RN02: el registro se conserva con su nuevo estado; no se elimina
        categoria.setEstado(estado);
        return null;
    }

    /**
     * Reglas comunes de alta y edición. 'idExcluido' es el id que no compite
     * consigo mismo al validar unicidad (edición).
     */
    private String validar(Categoria categoria, Long idExcluido) {
        if (categoria == null || categoria.getNombre() == null
                || com.example.nexo.shared.validation.TextoNormalizado.nombre(categoria.getNombre()).isEmpty()) {
            return "El nombre de la categoría es obligatorio.";
        }
        String nombre = com.example.nexo.shared.validation.TextoNormalizado.nombre(categoria.getNombre());
        if (nombre.length() < 3 || nombre.length() > 40) {
            return "El nombre de la categoría debe tener entre 3 y 40 caracteres.";
        }
        if (categoria.getDescripcion() != null
                && categoria.getDescripcion().trim().length() > 200) {
            return "La descripción no puede superar los 200 caracteres.";
        }
        if (!esEstadoValido(categoria.getEstado())) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // Unicidad ignorando mayúsculas y espacios (como en los usuarios)
        String normalizado = normalizarTexto(nombre);
        for (Categoria existente : repo.findAll()) {
            boolean misma = idExcluido != null && idExcluido.equals(existente.getId());
            if (!misma && normalizado.equals(normalizarTexto(existente.getNombre()))) {
                return "Ya existe una categoría con el nombre «" + existente.getNombre() + "».";
            }
        }
        return null; // sin errores
    }

    private boolean esEstadoValido(String estado) {
        return "Activo".equals(estado) || "Inactivo".equals(estado);
    }

    /** Guarda el nombre y la descripción sin los espacios sobrantes. */
    private void normalizar(Categoria categoria) {
        categoria.setNombre(com.example.nexo.shared.validation.TextoNormalizado.nombre(categoria.getNombre()));
        if (categoria.getDescripcion() != null) {
            categoria.setDescripcion(categoria.getDescripcion().trim());
        }
    }

    private String normalizarTexto(String texto) {
        return com.example.nexo.shared.validation.TextoNormalizado.clave(texto);
    }



}
