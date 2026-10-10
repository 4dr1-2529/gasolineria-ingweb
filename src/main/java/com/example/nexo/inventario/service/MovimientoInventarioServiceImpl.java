package com.example.nexo.inventario.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.nexo.compra.model.Compra;
import com.example.nexo.compra.service.CompraService;
import com.example.nexo.inventario.model.MovimientoInventario;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.service.ProductoService;
import com.example.nexo.usuario.service.UsuarioService;
import com.example.nexo.venta.model.Venta;
import com.example.nexo.venta.service.VentaService;


/**
 * Guarda los movimientos de inventario (entradas y salidas) en una colección
 * en memoria. No hay base de datos: la lista vive mientras la aplicación está
 * en ejecución y el estado se pierde al reiniciar.
 * Los datos iniciales son los seis movimientos de la versión 1 (MI001–MI006).
 * Sus litros ya están reflejados en el stock inicial de los productos
 * (1,990 / 980 / 3,950 L), por eso el arranque no vuelve a modificarlo.
 */
@Service
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final List<MovimientoInventario> movimientos = new ArrayList<>();

    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public MovimientoInventarioServiceImpl(ProductoService productoService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        // Datos de partida tomados de la versión 1 (libro de movimientos, P14)
        movimientos.add(new MovimientoInventario("MI001", "PR01", 1, "Entrada",
                new BigDecimal("100"), LocalDateTime.of(2026, 9, 10, 7, 0), "Compra C001"));
        movimientos.add(new MovimientoInventario("MI002", "PR02", 1, "Entrada",
                new BigDecimal("100"), LocalDateTime.of(2026, 9, 10, 7, 5), "Compra C001"));
        movimientos.add(new MovimientoInventario("MI003", "PR03", 1, "Entrada",
                new BigDecimal("100"), LocalDateTime.of(2026, 9, 10, 7, 10), "Compra C001"));
        movimientos.add(new MovimientoInventario("MI004", "PR01", 1, "Salida",
                new BigDecimal("10"), LocalDateTime.of(2026, 9, 10, 9, 0), "Venta V001"));
        movimientos.add(new MovimientoInventario("MI005", "PR02", 1, "Salida",
                new BigDecimal("20"), LocalDateTime.of(2026, 9, 10, 10, 0), "Venta V002"));
        movimientos.add(new MovimientoInventario("MI006", "PR03", 2, "Salida",
                new BigDecimal("50"), LocalDateTime.of(2026, 9, 10, 11, 0), "Venta V003"));
    }

    public List<MovimientoInventario> listaMovimientos() {
        return movimientos;
    }

    public List<MovimientoInventario> listaEntradas() {
        return filtrarPorTipo("Entrada");
    }

    public List<MovimientoInventario> listaSalidas() {
        return filtrarPorTipo("Salida");
    }

    public MovimientoInventario buscarMovimientoPorId(String id) {
        if (id == null) {
            return null;
        }
        for (MovimientoInventario movimiento : movimientos) {
            if (movimiento.getId().equals(id)) {
                return movimiento;
            }
        }
        return null;
    }

    /**
     * P12 · Entrada de combustible por compra o por ajuste (RN03).
     * Se comprueba, en este orden: 1) que el producto exista, 2) que la
     * cantidad sea mayor que cero, 3) que el responsable exista, 4) que el
     * motivo tenga entre 5 y 200 caracteres y 5) que la fecha exista.
     * Sólo si todo se cumple se crea el movimiento tipo Entrada y se suman
     * los litros al stock (el mismo objeto de ProductoService, sin
     * duplicarlo). Si alguna comprobación falla, devuelve el mensaje y no
     * cambia ningún dato en memoria.
     */
    public String registrarEntrada(MovimientoInventario movimiento) {
        Producto producto = buscarProducto(movimiento.getIdProducto());
        if (producto == null) {
            return "El producto seleccionado no existe.";
        }
        // RN02 · un producto Inactivo no recibe movimientos nuevos (P25)
        if (!"Activo".equals(producto.getEstado())) {
            return "El producto seleccionado está inactivo: no se registran movimientos sobre productos inactivos.";
        }
        String error = validarDatosDelMovimiento(movimiento);
        if (error != null) {
            return error;
        }

        movimiento.setId(siguienteId());
        movimiento.setTipoMovimiento("Entrada");
        movimientos.add(movimiento);

        // La entrada manual suma los litros al stock, una sola vez
        BigDecimal stock = producto.getStock().add(movimiento.getCantidad());
        if (stock.stripTrailingZeros().scale() <= 0) {
            // El stock entero se muestra sin decimales, como en la versión 1
            stock = stock.setScale(0);
        }
        producto.setStock(stock);

        return null;
    }

    /**
     * P13 · Salida física justificada (RN01 y RN03). Las mismas
     * comprobaciones de la entrada y, además, que la cantidad solicitada no
     * supere el stock disponible (RN01). Sólo si todo se cumple se crea el
     * movimiento tipo Salida y se descuentan los litros del stock. Si alguna
     * comprobación falla, devuelve el mensaje y no cambia ningún dato.
     */
    public String registrarSalida(MovimientoInventario movimiento) {
        Producto producto = buscarProducto(movimiento.getIdProducto());
        if (producto == null) {
            return "El producto seleccionado no existe.";
        }
        // RN02 · un producto Inactivo no recibe movimientos nuevos (P26)
        if (!"Activo".equals(producto.getEstado())) {
            return "El producto seleccionado está inactivo: no se registran movimientos sobre productos inactivos.";
        }
        String error = validarDatosDelMovimiento(movimiento);
        if (error != null) {
            return error;
        }
        if (movimiento.getCantidad().compareTo(producto.getStock()) > 0) {
            return "No hay stock suficiente para la salida: se solicitan " + movimiento.getCantidad()
                    + " L y sólo quedan " + producto.getStock() + " L de " + producto.getNombre() + ".";
        }

        movimiento.setId(siguienteId());
        movimiento.setTipoMovimiento("Salida");
        movimientos.add(movimiento);

        // La salida manual descuenta los litros del stock, una sola vez
        BigDecimal stock = producto.getStock().subtract(movimiento.getCantidad());
        if (stock.stripTrailingZeros().scale() <= 0) {
            // El stock entero se muestra sin decimales, como en la versión 1
            stock = stock.setScale(0);
        }
        producto.setStock(stock);

        return null;
    }

    /**
     * RN03 · Cada línea recibida en una compra genera su entrada de
     * inventario. Aquí sólo se registra el movimiento: el stock ya se sumó
     * en CompraService.crearCompra, así que no se vuelve a modificar.
     */
    public void registrarEntradaPorCompra(String idProducto, Integer idUsuario, BigDecimal cantidad,
            LocalDateTime fechaHora, String motivo) {
        movimientos.add(new MovimientoInventario(siguienteId(), idProducto, idUsuario,
                "Entrada", cantidad, fechaHora, motivo));
    }

    /**
     * RN03 · Cada venta confirmada genera su salida de inventario. Aquí
     * sólo se registra el movimiento: el stock ya se descontó en
     * VentaService.crearVenta, así que no se vuelve a modificar.
     */
    public void registrarSalidaPorVenta(String idProducto, Integer idUsuario, BigDecimal cantidad,
            LocalDateTime fechaHora, String motivo) {
        movimientos.add(new MovimientoInventario(siguienteId(), idProducto, idUsuario,
                "Salida", cantidad, fechaHora, motivo));
    }

    /**
     * Datos comunes de un movimiento manual: cantidad positiva (RN03),
     * responsable existente, motivo de 5 a 200 caracteres y fecha presente.
     * Devuelve null cuando todo está bien o, si no, el motivo del rechazo.
     */
    private String validarDatosDelMovimiento(MovimientoInventario movimiento) {
        if (movimiento.getCantidad() == null) {
            return "La cantidad de litros es obligatoria.";
        }
        BigDecimal cantidad = movimiento.getCantidad().setScale(2, RoundingMode.HALF_UP);
        if (cantidad.signum() <= 0) {
            return "La cantidad de litros debe ser mayor que cero.";
        }
        movimiento.setCantidad(cantidad);
        if (movimiento.getIdUsuario() == null || usuarioService.buscarUsuarioPorId(movimiento.getIdUsuario()) == null) {
            return "El responsable seleccionado no existe.";
        }
        // RN02 · una cuenta Inactiva tampoco se elige en operaciones nuevas
        if (!"Activo".equals(usuarioService.buscarUsuarioPorId(movimiento.getIdUsuario()).getEstado())) {
            return "El responsable seleccionado está inactivo: elija una cuenta activa.";
        }
        String motivo = movimiento.getMotivo() == null ? "" : movimiento.getMotivo().trim();
        if (motivo.length() < 5 || motivo.length() > 200) {
            return "El motivo debe tener entre 5 y 200 caracteres.";
        }
        movimiento.setMotivo(motivo);
        if (movimiento.getFechaHora() == null) {
            return "La fecha y hora de la operación son obligatorias.";
        }
        return null;
    }

    private List<MovimientoInventario> filtrarPorTipo(String tipo) {
        List<MovimientoInventario> propios = new ArrayList<>();
        for (MovimientoInventario movimiento : movimientos) {
            if (tipo.equals(movimiento.getTipoMovimiento())) {
                propios.add(movimiento);
            }
        }
        return propios;
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
     * Continúa la numeración de códigos de la versión 1: MI001–MI006 → MI007.
     */
    private String siguienteId() {
        int maximo = 0;
        for (MovimientoInventario movimiento : movimientos) {
            String id = movimiento.getId();
            if (id != null && id.startsWith("MI") && id.length() > 2) {
                try {
                    maximo = Math.max(maximo, Integer.parseInt(id.substring(2)));
                } catch (NumberFormatException e) {
                    // Un código con formato distinto no altera la numeración
                }
            }
        }
        return String.format("MI%03d", maximo + 1);
    }

}
