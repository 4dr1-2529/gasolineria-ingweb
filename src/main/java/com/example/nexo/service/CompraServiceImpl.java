package com.example.nexo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Compra;
import com.example.nexo.model.DetalleCompra;
import com.example.nexo.model.Producto;

/**
 * Guarda las compras y sus líneas de detalle en colecciones en memoria.
 * No hay base de datos: las listas viven mientras la aplicación está en
 * ejecución y el estado se pierde al reiniciar.
 * Los datos iniciales son la compra de referencia de la versión 1 (C001).
 */
@Service
public class CompraServiceImpl implements CompraService {

    private final List<Compra> compras = new ArrayList<>();
    private final List<DetalleCompra> detalles = new ArrayList<>();

    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final MovimientoInventarioService movimientoInventarioService;

    public CompraServiceImpl(ProductoService productoService, UsuarioService usuarioService,
            MovimientoInventarioService movimientoInventarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.movimientoInventarioService = movimientoInventarioService;
        // Datos de partida tomados de la versión 1 (compra C001 con sus tres líneas)
        compras.add(new Compra("C001", 1, LocalDateTime.of(2026, 9, 10, 7, 0),
                "Petroandes S.A.", new BigDecimal("1350.00"), "Confirmada"));
        detalles.add(new DetalleCompra(1, "C001", "PR01",
                new BigDecimal("100"), new BigDecimal("4.50"), new BigDecimal("450.00")));
        detalles.add(new DetalleCompra(2, "C001", "PR02",
                new BigDecimal("100"), new BigDecimal("5.40"), new BigDecimal("540.00")));
        detalles.add(new DetalleCompra(3, "C001", "PR03",
                new BigDecimal("100"), new BigDecimal("3.60"), new BigDecimal("360.00")));
    }

    public List<Compra> listaCompras() {
        return compras;
    }

    public Compra buscarCompraPorId(String id) {
        if (id == null) {
            return null;
        }
        for (Compra compra : compras) {
            if (compra.getId().equals(id)) {
                return compra;
            }
        }
        return null;
    }

    public List<DetalleCompra> listaDetalles() {
        return detalles;
    }

    public List<DetalleCompra> listaDetallesPorCompra(String idCompra) {
        List<DetalleCompra> propios = new ArrayList<>();
        if (idCompra == null) {
            return propios;
        }
        for (DetalleCompra detalle : detalles) {
            if (detalle.getIdCompra().equals(idCompra)) {
                propios.add(detalle);
            }
        }
        return propios;
    }

    /**
     * RN03 y RN04 · Al registrar una compra se hace, en este
     * orden: 1) se crea la compra confirmada, 2) se crea su línea de detalle,
     * 3) se suman los litros recibidos al stock del producto (el mismo objeto de
     * ProductoService, sin duplicarlo) y 4) se registra la entrada de inventario
     * correspondiente, sin volver a tocar el stock.
     * La compra queda además documentada como origen del egreso económico de
     * su importe; ese MovimientoCaja lo consulta el módulo Finanzas.
     * RN03 · Litros, precios e importes deben ser mayores que cero y con dos
     * decimales como máximo; los valores fuera de ese rango rechazan la compra
     * sin modificar ningún dato en memoria.
     */
    public String crearCompra(Compra compra, DetalleCompra detalle, LocalDate fecha) {
        if (fecha == null) {
            return "La fecha de la compra es obligatoria.";
        }
        String proveedor = compra.getProveedor() == null ? "" : compra.getProveedor().trim();
        if (proveedor.length() < 3 || proveedor.length() > 60) {
            return "El proveedor debe tener entre 3 y 60 caracteres.";
        }
        if (buscarProducto(detalle.getIdProducto()) == null) {
            return "El producto seleccionado no existe.";
        }
        if (detalle.getCantidad() == null || detalle.getPrecioCompra() == null) {
            return "La cantidad de litros y el precio de compra son obligatorios.";
        }
        BigDecimal cantidad = detalle.getCantidad().setScale(2, RoundingMode.HALF_UP);
        BigDecimal precio = detalle.getPrecioCompra().setScale(2, RoundingMode.HALF_UP);
        if (cantidad.signum() <= 0) {
            return "La cantidad de litros debe ser mayor que cero.";
        }
        if (precio.signum() <= 0) {
            return "El precio de compra debe ser mayor que cero.";
        }
        if (compra.getIdUsuario() == null || usuarioService.buscarUsuarioPorId(compra.getIdUsuario()) == null) {
            return "El responsable seleccionado no existe.";
        }

        BigDecimal subtotal = cantidad.multiply(precio).setScale(2, RoundingMode.HALF_UP);

        // 1) La compra: una sola línea por formulario, por eso el total es su subtotal
        compra.setProveedor(proveedor);
        compra.setId(siguienteId());
        // La fecha la toma el servidor; no se digita la hora
        compra.setFechaHora(LocalDateTime.of(fecha, LocalTime.now()));
        compra.setTotal(subtotal);
        compra.setEstado("Confirmada");
        compras.add(compra);

        // 2) La línea de detalle, ligada a la compra por su identificador
        detalle.setId(siguienteIdDetalle());
        detalle.setIdCompra(compra.getId());
        detalle.setCantidad(cantidad);
        detalle.setPrecioCompra(precio);
        detalle.setSubtotal(subtotal);
        detalles.add(detalle);

        // 3) Stock: se modifica el mismo producto en memoria, sin duplicarlo
        Producto producto = buscarProducto(detalle.getIdProducto());
        BigDecimal stock = producto.getStock().add(cantidad);
        if (stock.stripTrailingZeros().scale() <= 0) {
            // El stock entero se muestra sin decimales, como en la versión 1
            stock = stock.setScale(0);
        }
        producto.setStock(stock);

        // 4) RN03 · La compra genera la entrada de inventario de su línea.
        //    El stock ya se sumó en el paso 3: aquí sólo se registra
        //    el movimiento, para no descontarlo ni sumarlo dos veces
        movimientoInventarioService.registrarEntradaPorCompra(detalle.getIdProducto(),
                compra.getIdUsuario(), cantidad, compra.getFechaHora(), "Compra " + compra.getId());

        return null;
    }

    private Producto buscarProducto(String id) {
        if (id == null) {
            return null;
        }
        for (Producto producto : productoService.listaProductos()) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Continúa la numeración de códigos de la versión 1: C001 → C002.
     */
    private String siguienteId() {
        int maximo = 0;
        for (Compra compra : compras) {
            String id = compra.getId();
            if (id != null && id.startsWith("C") && id.length() > 1) {
                try {
                    maximo = Math.max(maximo, Integer.parseInt(id.substring(1)));
                } catch (NumberFormatException e) {
                    // Un código con formato distinto no altera la numeración
                }
            }
        }
        return String.format("C%03d", maximo + 1);
    }

    /**
     * Numeración correlativa de las líneas: 1, 2, 3 → 4.
     */
    private Integer siguienteIdDetalle() {
        int maximo = 0;
        for (DetalleCompra detalle : detalles) {
            if (detalle.getId() != null) {
                maximo = Math.max(maximo, detalle.getId());
            }
        }
        return maximo + 1;
    }

}
