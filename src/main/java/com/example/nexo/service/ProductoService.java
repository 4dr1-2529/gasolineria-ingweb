package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Producto;

/**
 * Catálogo de combustibles (F09 registrar, F10 consultar, F11 editar,
 * F12 activar/desactivar).
 * Cada método de escritura devuelve un mensaje de error en español, o
 * null si la operación se completó: el controller lo convierte en aviso.
 */
public interface ProductoService {

    public List<Producto> listaProductos();

    /** Busca por código (PR01…); devuelve null si no existe. */
    public Producto buscarProductoPorId(String id);

    /** F09 · alta de un combustible; devuelve el error o null si se registró. */
    public String crearProducto(Producto producto);

    /** F11 · edición conservando el código; devuelve el error o null. */
    public String editarProducto(Producto producto);

    /** F12 · RN02 · cambia el estado sin eliminar el registro. */
    public String cambiarEstadoProducto(String id, String estado);

}
