package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.DetalleVenta;
import com.example.nexo.model.Venta;

public interface VentaService {

    public List<Venta> listaVentas();

    public Venta buscarVentaPorId(String id);

    public List<DetalleVenta> listaDetalles();

    public List<DetalleVenta> listaDetallesPorVenta(String idVenta);

    /**
     * Registra una venta confirmada con su detalle (RN01, RN02, RN03 y RN04):
     * crea la venta, crea la línea de detalle y descuenta los litros del stock
     * del producto. El precio unitario se toma del producto existente.
     * Devuelve null cuando la venta quedó registrada, o el mensaje de error
     * cuando la venta fue rechazada (no se modifica ningún dato en memoria).
     */
    public String crearVenta(Venta venta, DetalleVenta detalle);

}
