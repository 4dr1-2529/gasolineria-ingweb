package com.example.nexo.categoria.service;

import java.util.List;
import com.example.nexo.categoria.model.Categoria;


/**
 * Cat��logo de familias de combustible (F04 registrar, F05 consultar,
 * F07 editar, F08 activar/desactivar).
 * Cada m��todo de escritura devuelve un mensaje de error en espa��ol, o
 * null si la operaci��n se complet��: el controller lo convierte en aviso.
 */
public interface CategoriaService {

    public List<Categoria> listaCategorias();

    /** Busca por identificador; devuelve null si no existe. */
    public Categoria buscarCategoriaPorId(Integer id);

    /** F04 · alta de una categor��a; devuelve el error o null si se registr��. */
    public String crearCategoria(Categoria categoria);

    /** F07 · edici��n conservando el id; devuelve el error o null si se edit��. */
    public String editarCategoria(Categoria categoria);

    /** F08 · RN02 · cambia el estado sin eliminar el registro. */
    public String cambiarEstadoCategoria(Integer id, String estado);

}
