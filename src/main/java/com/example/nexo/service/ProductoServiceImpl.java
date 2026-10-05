package com.example.nexo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Producto;

/**
 * Guarda los productos (combustibles) en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son los tres combustibles de la versión 1.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final List<Producto> productos = new ArrayList<>();

    public ProductoServiceImpl() {
        // Datos de partida tomados de la versión 1 (catálogo de combustibles)
        productos.add(new Producto("PR01", 1, "Gasolina Regular", "Litro",
                new BigDecimal("5.00"), new BigDecimal("1990"), "Activo"));
        productos.add(new Producto("PR02", 1, "Gasolina Premium", "Litro",
                new BigDecimal("6.00"), new BigDecimal("980"), "Activo"));
        productos.add(new Producto("PR03", 2, "Diésel", "Litro",
                new BigDecimal("4.00"), new BigDecimal("3950"), "Activo"));
    }

    public List<Producto> listaProductos() {
        return productos;
    }

    public void crearProducto(Producto producto) {
        producto.setId(siguienteId());
        productos.add(producto);
    }

    /**
     * Continúa la numeración de códigos de la versión 1: PR01, PR02, PR03 → PR04.
     */
    private String siguienteId() {
        int maximo = 0;
        for (Producto producto : productos) {
            String id = producto.getId();
            if (id != null && id.startsWith("PR")) {
                try {
                    maximo = Math.max(maximo, Integer.parseInt(id.substring(2)));
                } catch (NumberFormatException e) {
                    // Se ignora un código que no siga el patrón PRnn
                }
            }
        }
        return String.format("PR%02d", maximo + 1);
    }

}
