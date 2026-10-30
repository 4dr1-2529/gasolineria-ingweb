package com.example.nexo.producto.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.categoria.model.Categoria;
import com.example.nexo.categoria.service.CategoriaService;
import com.example.nexo.producto.model.Producto;


/** Servicio persistente: conserva validación, baja lógica e historial. */
@Service
@org.springframework.transaction.annotation.Transactional
public class ProductoServiceImpl implements ProductoService {

    private final com.example.nexo.producto.repository.ProductoRepository repo;
    private final CategoriaService categoriaService;



    public ProductoServiceImpl(CategoriaService categoriaService, com.example.nexo.producto.repository.ProductoRepository repo) {
        this.repo=repo;
        this.categoriaService = categoriaService;
    }

    public List<Producto> listaProductos() {
        return repo.findAll();
    }

    public Producto buscarProductoPorId(String id) {return id==null?null:repo.buscarCodigo(id).orElse(null);}

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String crearProducto(Producto producto) {
        String error = validar(producto, null, true);
        if (error != null) {
            return error;
        }
        normalizar(producto);
        producto.setClave(null);
        producto.setId(com.example.nexo.shared.persistence.Codigos.nuevo("PR",10));
        producto.setCategoria(categoriaService.buscarCategoriaPorId(producto.getIdCategoria()));
        repo.save(producto);
        return null; // alta completada
    }

    public boolean disponibleParaOperacion(String id) {
        Producto producto = buscarProductoPorId(id);
        if (producto == null || !"Activo".equals(producto.getEstado())) return false;
        Categoria categoria = categoriaService.buscarCategoriaPorId(producto.getIdCategoria());
        return categoria != null && "Activo".equals(categoria.getEstado());
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String editarProducto(Producto producto) {
        Producto existente = repo.bloquear(producto.getId()).orElse(null);
        if (existente == null) {
            return "No se encontró el combustible solicitado.";
        }
        // F11 modifica atributos comerciales; las variaciones físicas pasan por Inventario.
        producto.setStock(existente.getStock());
        // En la edición la categoría actual puede conservarse aunque esté
        // Inactiva (RN02: el historial se conserva); sólo se exige que exista.
        String error = validar(producto, producto.getId(), false);
        if (error != null) {
            return error;
        }
        normalizar(producto);
        existente.setNombre(producto.getNombre());
        existente.setCategoria(categoriaService.buscarCategoriaPorId(producto.getIdCategoria()));
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setPrecioActual(producto.getPrecioActual());
        existente.setEstado(producto.getEstado());
        return null; // edición completada: se conserva el código y el historial
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
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
                || com.example.nexo.shared.validation.TextoNormalizado.nombre(producto.getNombre()).isEmpty()) {
            return "El nombre del combustible es obligatorio.";
        }
        String nombre = com.example.nexo.shared.validation.TextoNormalizado.nombre(producto.getNombre());
        if (nombre.length() < 3 || nombre.length() > 40) {
            return "El nombre del combustible debe tener entre 3 y 40 caracteres.";
        }
        // Unicidad ignorando mayúsculas y espacios (como en los usuarios)
        String normalizado = normalizarTexto(nombre);
        for (Producto existente : repo.findAll()) {
            boolean mismo = idExcluido != null && idExcluido.equals(existente.getId());
            if (!mismo && java.util.Objects.equals(producto.getIdCategoria(), existente.getIdCategoria())
                    && normalizado.equals(normalizarTexto(existente.getNombre()))) {
                return "Ya existe un combustible con el nombre «" + existente.getNombre() + "».";
            }
        }
        Categoria categoria = categoriaService.buscarCategoriaPorId(producto.getIdCategoria());
        if (categoria == null) {
            return "La categoría seleccionada no existe.";
        }
        Producto anterior = idExcluido == null ? null : buscarProductoPorId(idExcluido);
        boolean cambiaCategoria = anterior == null
                || !java.util.Objects.equals(anterior.getIdCategoria(), producto.getIdCategoria());
        if ((categoriaActiva || cambiaCategoria) && !"Activo".equals(categoria.getEstado())) {
            return "La categoría «" + categoria.getNombre()
                    + "» está Inactiva y no puede usarse en un combustible nuevo.";
        }
        if (!"L".equals(producto.getUnidadMedida())) {
            return "La unidad de medida debe ser L (litros).";
        }
        if (producto.getPrecioActual() == null
                || producto.getPrecioActual().compareTo(BigDecimal.ZERO) <= 0) {
            return "El precio por litro debe ser mayor a cero.";
        }
        if (producto.getPrecioActual().stripTrailingZeros().scale() > 2
                || producto.getPrecioActual().compareTo(new BigDecimal("9999999999.99")) > 0) {
            return "El precio debe ajustarse a DECIMAL(12,2).";
        }
        // RN01: el stock nunca queda negativo; en el alta no se admite negativo
        if (producto.getStock() == null
                || producto.getStock().compareTo(BigDecimal.ZERO) < 0) {
            return "El stock no puede ser negativo.";
        }
        if (producto.getStock().stripTrailingZeros().scale() > 3
                || producto.getStock().compareTo(new BigDecimal("999999999.999")) > 0) {
            return "El stock debe ajustarse a DECIMAL(12,3).";
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
        producto.setNombre(com.example.nexo.shared.validation.TextoNormalizado.nombre(producto.getNombre()));
    }

    private String normalizarTexto(String texto) {
        return com.example.nexo.shared.validation.TextoNormalizado.clave(texto);
    }

    /**
     * Continúa la numeración de códigos de la versión 1: PR01, PR02, PR03 → PR04.
     */


}
