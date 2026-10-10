package com.example.nexo.venta.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.finanzas.model.MovimientoCaja;
import com.example.nexo.inventario.service.MovimientoInventarioService;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.service.ProductoService;
import com.example.nexo.usuario.service.UsuarioService;
import com.example.nexo.venta.model.DetalleVenta;
import com.example.nexo.venta.model.Venta;


/**
 * Guarda las ventas y sus líneas de detalle en colecciones en memoria.
 * No hay base de datos: las listas viven mientras la aplicación está en
 * ejecución y el estado se pierde al reiniciar.
 * Los datos iniciales son las tres ventas de referencia de la versión 1
 * (V001, V002 y V003). Los litros de esas ventas ya están descontados en la
 * existencia inicial de los productos (1,990 / 980 / 3,950 L del modelo de
 * negocio), por eso el arranque no vuelve a descontarlos.
 */
@Service
public class VentaServiceImpl implements VentaService {

    private final List<Venta> ventas = new ArrayList<>();
    private final List<DetalleVenta> detalles = new ArrayList<>();

    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final MovimientoInventarioService movimientoInventarioService;

    public VentaServiceImpl(ProductoService productoService, UsuarioService usuarioService,
            MovimientoInventarioService movimientoInventarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.movimientoInventarioService = movimientoInventarioService;
        // Datos de partida tomados de la versión 1 (ventas del 10/09/2026)
        ventas.add(new Venta("V001", 1, LocalDateTime.of(2026, 9, 10, 9, 0),
                new BigDecimal("50.00"), "Confirmada"));
        ventas.add(new Venta("V002", 1, LocalDateTime.of(2026, 9, 10, 10, 0),
                new BigDecimal("120.00"), "Confirmada"));
        ventas.add(new Venta("V003", 2, LocalDateTime.of(2026, 9, 10, 11, 0),
                new BigDecimal("200.00"), "Confirmada"));
        detalles.add(new DetalleVenta(1, "V001", "PR01",
                new BigDecimal("10"), new BigDecimal("5.00"), new BigDecimal("50.00")));
        detalles.add(new DetalleVenta(2, "V002", "PR02",
                new BigDecimal("20"), new BigDecimal("6.00"), new BigDecimal("120.00")));
        detalles.add(new DetalleVenta(3, "V003", "PR03",
                new BigDecimal("50"), new BigDecimal("4.00"), new BigDecimal("200.00")));
    }

    public List<Venta> listaVentas() {
        return ventas;
    }

    public Venta buscarVentaPorId(String id) {
        if (id == null) {
            return null;
        }
        for (Venta venta : ventas) {
            if (venta.getId().equals(id)) {
                return venta;
            }
        }
        return null;
    }

    public List<DetalleVenta> listaDetalles() {
        return detalles;
    }

    public List<DetalleVenta> listaDetallesPorVenta(String idVenta) {
        List<DetalleVenta> propios = new ArrayList<>();
        if (idVenta == null) {
            return propios;
        }
        for (DetalleVenta detalle : detalles) {
            if (detalle.getIdVenta().equals(idVenta)) {
                propios.add(detalle);
            }
        }
        return propios;
    }

    /**
     * RN01, RN02, RN03 y RN04 · Venta y descuento de combustible. Al registrar
     * una venta se comprueba, en este orden: 1) que el producto exista,
     * 2) que esté Activo (RN02), 3) que la cantidad sea mayor que cero (RN03),
     * 4) que no supere el stock disponible (RN01) y 5) que el operador exista.
     * Sólo si todo se cumple se crea la venta confirmada, se crea su línea de
     * detalle con el precio unitario del producto, se descuentan los litros
     * del stock (el mismo objeto de ProductoService, sin duplicarlo) y se
     * registra la salida de inventario correspondiente, sin volver a tocar
     * el stock. RN04 · La venta queda documentada como origen del único
     * ingreso de caja que le corresponde; ese MovimientoCaja lo consulta el
     * módulo Finanzas. Si alguna comprobación falla, la venta es
     * rechazada sin crear venta, sin crear detalle, sin tocar el stock y sin
     * registrar movimientos.
     */
    public String crearVenta(Venta venta, DetalleVenta detalle) {
        Producto producto = buscarProducto(detalle.getIdProducto());
        if (producto == null) {
            return "El producto seleccionado no existe.";
        }
        if (!"Activo".equals(producto.getEstado())) {
            return "El producto seleccionado está inactivo: sólo los combustibles activos pueden venderse.";
        }
        if (detalle.getCantidad() == null) {
            return "La cantidad de litros es obligatoria.";
        }
        BigDecimal cantidad = detalle.getCantidad().setScale(2, RoundingMode.HALF_UP);
        if (cantidad.signum() <= 0) {
            return "La cantidad de litros debe ser mayor que cero.";
        }
        if (cantidad.compareTo(producto.getStock()) > 0) {
            return "No hay stock suficiente: se solicitan " + cantidad + " L y sólo quedan "
                    + producto.getStock() + " L de " + producto.getNombre() + ".";
        }
        if (venta.getIdUsuario() == null || usuarioService.buscarUsuarioPorId(venta.getIdUsuario()) == null) {
            return "El operador seleccionado no existe.";
        }
        // RN02 · una cuenta Inactiva tampoco se elige en operaciones nuevas
        if (!"Activo".equals(usuarioService.buscarUsuarioPorId(venta.getIdUsuario()).getEstado())) {
            return "El operador seleccionado está inactivo: elija una cuenta activa.";
        }
        BigDecimal precio = producto.getPrecioActual();
        if (precio == null || precio.signum() <= 0) {
            return "El precio del producto no es válido.";
        }
        precio = precio.setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = cantidad.multiply(precio).setScale(2, RoundingMode.HALF_UP);

        // 1) La venta: una sola línea por formulario, por eso el total es su subtotal
        venta.setId(siguienteId());
        // La fecha y la hora las toma el servidor; no se digitan
        venta.setFechaHora(LocalDateTime.now());
        venta.setTotal(subtotal);
        venta.setEstado("Confirmada");
        ventas.add(venta);

        // 2) La línea de detalle, ligada a la venta por su identificador
        detalle.setId(siguienteIdDetalle());
        detalle.setIdVenta(venta.getId());
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(subtotal);
        detalles.add(detalle);

        // 3) Stock: se descuentan los litros del mismo producto en memoria
        BigDecimal stock = producto.getStock().subtract(cantidad);
        if (stock.stripTrailingZeros().scale() <= 0) {
            // El stock entero se muestra sin decimales, como en la versión 1
            stock = stock.setScale(0);
        }
        producto.setStock(stock);

        // 4) RN03 · La venta genera la salida de inventario de sus litros.
        //    El stock ya se descontó en el paso 3: aquí sólo se registra
        //    el movimiento, para no descontarlo dos veces
        movimientoInventarioService.registrarSalidaPorVenta(detalle.getIdProducto(),
                venta.getIdUsuario(), cantidad, venta.getFechaHora(), "Venta " + venta.getId());

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
     * Continúa la numeración de códigos de la versión 1: V001, V002, V003 → V004.
     */
    private String siguienteId() {
        int maximo = 0;
        for (Venta venta : ventas) {
            String id = venta.getId();
            if (id != null && id.startsWith("V") && id.length() > 1) {
                try {
                    maximo = Math.max(maximo, Integer.parseInt(id.substring(1)));
                } catch (NumberFormatException e) {
                    // Un código con formato distinto no altera la numeración
                }
            }
        }
        return String.format("V%03d", maximo + 1);
    }

    /**
     * Numeración correlativa de las líneas: 1, 2, 3 → 4.
     */
    private Integer siguienteIdDetalle() {
        int maximo = 0;
        for (DetalleVenta detalle : detalles) {
            if (detalle.getId() != null) {
                maximo = Math.max(maximo, detalle.getId());
            }
        }
        return maximo + 1;
    }

}
