package com.example.nexo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.nexo.model.Compra;
import com.example.nexo.model.DetalleCompra;

public interface CompraService {

    public List<Compra> listaCompras();

    public Compra buscarCompraPorId(String id);

    public List<DetalleCompra> listaDetalles();

    public List<DetalleCompra> listaDetallesPorCompra(String idCompra);

    /**
     * Registra una compra confirmada con su detalle (RN03 y RN04): crea la
     * compra, crea la línea de detalle y suma los litros al stock del producto.
     * Devuelve null cuando la compra quedó registrada, o el mensaje de error
     * cuando la compra fue rechazada (no se modifica ningún dato).
     */
    public String crearCompra(Compra compra, DetalleCompra detalle, LocalDate fecha);

}
