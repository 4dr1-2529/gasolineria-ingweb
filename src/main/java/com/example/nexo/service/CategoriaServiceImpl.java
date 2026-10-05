package com.example.nexo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Categoria;

/**
 * Guarda las categorías en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final List<Categoria> categorias = new ArrayList<>();

    public CategoriaServiceImpl() {
        // Datos de partida tomados de la versión 1 (Categorías: C01 y C02)
        categorias.add(new Categoria(1, "Gasolinas"));
        categorias.add(new Categoria(2, "Diésel"));
    }

    public List<Categoria> listaCategorias() {
        return categorias;
    }

    public void crearCategoria(Categoria categoria) {
        categoria.setId(siguienteId());
        categorias.add(categoria);
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
