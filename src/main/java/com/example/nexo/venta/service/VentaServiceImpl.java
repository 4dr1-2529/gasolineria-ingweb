package com.example.nexo.venta.service;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.venta.model.*;
import com.example.nexo.venta.repository.*;
import com.example.nexo.shared.service.OperacionCombustible;
@Service @Transactional(readOnly=true)
public class VentaServiceImpl implements VentaService {
 private final VentaRepository repo; private final DetalleVentaRepository detalles; private final OperacionCombustible operaciones;
 public VentaServiceImpl(VentaRepository repo,DetalleVentaRepository detalles,OperacionCombustible operaciones) {this.repo=repo;this.detalles=detalles;this.operaciones=operaciones;}
 public List<Venta> listaVentas() {return repo.findAll();}
 public Venta buscarVentaPorId(String id) {return id==null?null:repo.buscarCodigo(id).orElse(null);}
 public List<DetalleVenta> listaDetalles() {return detalles.findAll();}
 public List<DetalleVenta> listaDetallesPorVenta(String id) {return detalles.porOperacion(id);}
 @Transactional
 public String crearVenta(Venta cabecera,DetalleVenta detalle) {operaciones.venta(cabecera,List.of(detalle));return null;}
 @Transactional
 public String crearVenta(Venta cabecera,List<DetalleVenta> lineas) {operaciones.venta(cabecera,lineas);return null;}
}
