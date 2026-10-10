package com.example.nexo.producto.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.categoria.model.Categoria;
import com.example.nexo.categoria.service.CategoriaService;
import com.example.nexo.producto.model.Producto;


/**
 * Guarda los productos (combustibles) en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son los tres combustibles de la versión 1.
 *
 * Validaciones de servidor (F09 y F11): nombre obligatorio de 3 a 40
 * caracteres, único ignorando mayúsculas y espacios; categoría existente y
 * Activa en el alta; unidad Litro; precio mayor que cero; stock no negativo
 * (RN01); estado del catálogo (Activo / Inactivo).
 * RN02: nunca se elimina un combustible, sólo se cambia su estado (F12).
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private final CategoriaService categoriaService;

    public ProductoServiceImpl(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
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

    public Producto buscarProductoPorId(String id) {
        if (id == null) {
            return null;
        }
        for (Producto producto : productos) {
            if (id.equals(producto.getId())) {
                return producto;
            }
        }
        return null;
    }

    public String crearProducto(Producto producto) {
        String error = validar(producto, null, true);
        if (error != null) {
            return error;
        }
        normalizar(producto);
        producto.setId(siguienteId());
        productos.add(producto);
        return null; // alta completada
    }

    public String editarProducto(Producto producto) {
        Producto existente = buscarProductoPorId(producto.getId());
        if (existente == null) {
            return "No se encontró el combustible solicitado.";
        }
        // En la edición la categoría actual puede conservarse aunque esté
        // Inactiva (RN02: el historial se conserva); sólo se exige que exista.
        String error = validar(producto, producto.getId(), false);
        if (error != null) {
            return error;
        }
        normalizar(producto);
        existente.setNombre(producto.getNombre());
        existente.setIdCategoria(producto.getIdCategoria());
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setPrecioActual(producto.getPrecioActual());
        existente.setStock(producto.getStock());
        existente.setEstado(producto.getEstado());
        return null; // edición completada: se conserva el código y el historial
    }

    public String cambiarEstadoProducto(String id, String estado) {
        Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            return "No se encontró el combustible solicitado.";
        }
        if (!esEstadoValido(estado)) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // RN02: el registro se conserva con su nuevo estado; no se elimina
        producto.setEstado(estado);
        return null;
    }

    /**
     * Reglas comunes de alta y edición. 'idExcluido' es el código que no
     * compite consigo mismo al validar unicidad (edición).
     * 'categoriaActiva' marca si además la categoría debe estar Activa (alta).
     */
    private String validar(Producto producto, String idExcluido, boolean categoriaActiva) {
        if (producto == null || producto.getNombre() == null
                || producto.getNombre().trim().isEmpty()) {
            return "El nombre del combustible es obligatorio.";
        }
        String nombre = producto.getNombre().trim();
        if (nombre.length() < 3 || nombre.length() > 40) {
            return "El nombre del combustible debe tener entre 3 y 40 caracteres.";
        }
        // Unicidad ignorando mayúsculas y espacios (como en los usuarios)
        String normalizado = normalizarTexto(nombre);
        for (Producto existente : productos) {
            boolean mismo = idExcluido != null && idExcluido.equals(existente.getId());
            if (!mismo && normalizado.equals(normalizarTexto(existente.getNombre()))) {
                return "Ya existe un combustible con el nombre «" + existente.getNombre() + "».";
            }
        }
        Categoria categoria = categoriaService.buscarCategoriaPorId(producto.getIdCategoria());
        if (categoria == null) {
            return "La categoría seleccionada no existe.";
        }
        if (categoriaActiva && !"Activo".equals(categoria.getEstado())) {
            return "La categoría «" + categoria.getNombre()
                    + "» está Inactiva y no puede usarse en un combustible nuevo.";
        }
        if (!"Litro".equals(producto.getUnidadMedida())) {
            return "La unidad de medida debe ser Litro.";
        }
        if (producto.getPrecioActual() == null
                || producto.getPrecioActual().compareTo(BigDecimal.ZERO) <= 0) {
            return "El precio por litro debe ser mayor a cero.";
        }
        // RN01: el stock nunca queda negativo; en el alta no se admite negativo
        if (producto.getStock() == null
                || producto.getStock().compareTo(BigDecimal.ZERO) < 0) {
            return "El stock no puede ser negativo.";
        }
        if (!esEstadoValido(producto.getEstado())) {
            return "El estado debe ser Activo o Inactivo.";
        }
        return null; // sin errores
    }

    private boolean esEstadoValido(String estado) {
        return "Activo".equals(estado) || "Inactivo".equals(estado);
    }

    /** Guarda el nombre sin los espacios sobrantes. */
    private void normalizar(Producto producto) {
        producto.setNombre(producto.getNombre().trim());
    }

    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase().replaceAll("\\s+", " ");
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
                    int numero = Integer.parseInt(id.substring(2));
                    if (numero > maximo) {
                        maximo = numero;
                    }
                } catch (NumberFormatException excepcion) {
                    // Un código fuera del patrón no altera la numeración
                }
            }
        }
        return String.format("PR%02d", maximo + 1);
    }

}
