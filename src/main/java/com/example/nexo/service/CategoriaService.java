package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Categoria;

public interface CategoriaService {

    public List<Categoria> listaCategorias();

    public void crearCategoria(Categoria categoria);

}
