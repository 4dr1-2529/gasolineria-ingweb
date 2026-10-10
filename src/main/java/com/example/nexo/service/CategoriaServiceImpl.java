package com.example.nexo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Categoria;

/**
 * Guarda las categorías en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 *
 * Validaciones de servidor (F04 y F07): nombre obligatorio de 3 a 40
 * caracteres, único ignorando mayúsculas y espacios; descripción opcional
 * de hasta 200 caracteres; estado del catálogo (Activo / Inactivo).
 * RN02: nunca se elimina una categoría, sólo se cambia su estado (F08).
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final List<Categoria> categorias = new ArrayList<>();

    public CategoriaServiceImpl() {
        // Datos de partida tomados de la versión 1 (Categorías: C01 y C02)
        categorias.add(new Categoria(1, "Gasolinas",
                "Combustibles para motores a gasolina", "Activo"));
        categorias.add(new Categoria(2, "Diésel",
                "Combustible para motores diésel", "Activo"));
    }

    public List<Categoria> listaCategorias() {
        return categorias;
    }

    public Categoria buscarCategoriaPorId(Integer id) {
        if (id == null) {
            return null;
        }
        for (Categoria categoria : categorias) {
            if (id.equals(categoria.getId())) {
                return categoria;
            }
        }
        return null;
    }

    public String crearCategoria(Categoria categoria) {
        String error = validar(categoria, null);
        if (error != null) {
            return error;
        }
        normalizar(categoria);
        categoria.setId(siguienteId());
        categorias.add(categoria);
        return null; // alta completada
    }

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

    public String cambiarEstadoCategoria(Integer id, String estado) {
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
    private String validar(Categoria categoria, Integer idExcluido) {
        if (categoria == null || categoria.getNombre() == null
                || categoria.getNombre().trim().isEmpty()) {
            return "El nombre de la categoría es obligatorio.";
        }
        String nombre = categoria.getNombre().trim();
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
        for (Categoria existente : categorias) {
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
        categoria.setNombre(categoria.getNombre().trim());
        if (categoria.getDescripcion() != null) {
            categoria.setDescripcion(categoria.getDescripcion().trim());
        }
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private int siguienteId() {
        int maximo = 0;
        for (Categoria categoria : categorias) {
            if (categoria.getId() != null && categoria.getId() > maximo) {
                maximo = categoria.getId();
            }
        }
        return maximo + 1;
    }

}
