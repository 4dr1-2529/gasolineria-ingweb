package com.example.nexo.compra.service;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.compra.model.*;
import com.example.nexo.compra.repository.*;
import com.example.nexo.shared.service.OperacionCombustible;
@Service @Transactional(readOnly=true)
public class CompraServiceImpl implements CompraService {
 private final CompraRepository repo; private final DetalleCompraRepository detalles; private final OperacionCombustible operaciones;
 public CompraServiceImpl(CompraRepository repo,DetalleCompraRepository detalles,OperacionCombustible operaciones) {this.repo=repo;this.detalles=detalles;this.operaciones=operaciones;}
 public List<Compra> listaCompras() {return repo.findAll();}
 public Compra buscarCompraPorId(String id) {return id==null?null:repo.buscarCodigo(id).orElse(null);}
 public List<DetalleCompra> listaDetalles() {return detalles.findAll();}
 public List<DetalleCompra> listaDetallesPorCompra(String id) {return detalles.porOperacion(id);}
 @Transactional
 public String crearCompra(Compra cabecera,DetalleCompra detalle) {operaciones.compra(cabecera,List.of(detalle));return null;}
 @Transactional
 public String crearCompra(Compra cabecera,List<DetalleCompra> lineas) {operaciones.compra(cabecera,lineas);return null;}
 public List<Compra> filtrar(Long proveedor,Long responsable,java.time.LocalDate desde,java.time.LocalDate hasta,String estado,String producto) {
  com.example.nexo.shared.validation.ReglaNegocioException.exigir(desde==null || hasta==null || !desde.isAfter(hasta),"La fecha inicial no puede ser posterior a la final.");
  return repo.findAll().stream().filter(c->proveedor==null || proveedor.equals(c.getIdProveedor()))
   .filter(c->responsable==null || responsable.equals(c.getIdUsuario()))
   .filter(c->desde==null || !c.getFechaHora().toLocalDate().isBefore(desde)).filter(c->hasta==null || !c.getFechaHora().toLocalDate().isAfter(hasta))
   .filter(c->estado==null || estado.isBlank() || estado.equals(c.getEstado()))
   .filter(c->producto==null || producto.isBlank() || detalles.porOperacion(c.getId()).stream().anyMatch(d->producto.equals(d.getIdProducto()))).toList();
 }
}
