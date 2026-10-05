package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Producto;

public interface ProductoService {

    public List<Producto> listaProductos();

    public void crearProducto(Producto producto);

}
