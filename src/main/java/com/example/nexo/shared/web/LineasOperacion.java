package com.example.nexo.shared.web;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.nexo.compra.model.DetalleCompra;
import com.example.nexo.venta.model.DetalleVenta;
/** DTO de formulario; esta colección no almacena operaciones del sistema. */
public class LineasOperacion {
    private List<Linea> lineas=new ArrayList<>();
    public List<Linea> getLineas(){return lineas;} public void setLineas(List<Linea> valor){lineas=valor;}
    public static class Linea {
        private String idProducto;private BigDecimal cantidad;private BigDecimal precioCompra;
        public String getIdProducto(){return idProducto;} public void setIdProducto(String v){idProducto=v;}
        public BigDecimal getCantidad(){return cantidad;} public void setCantidad(BigDecimal v){cantidad=v;}
        public BigDecimal getPrecioCompra(){return precioCompra;} public void setPrecioCompra(BigDecimal v){precioCompra=v;}
        boolean vacia(){return (idProducto==null || idProducto.isBlank()) && cantidad==null && precioCompra==null;}
    }
    public List<DetalleCompra> compras(DetalleCompra primera){
        List<DetalleCompra> resultado=new ArrayList<>();resultado.add(primera);
        if(lineas!=null)for(var l:lineas)if(l!=null && !l.vacia())resultado.add(new DetalleCompra(null,null,l.idProducto,l.cantidad,l.precioCompra,null));return resultado;
    }
    public List<DetalleVenta> ventas(DetalleVenta primera){
        List<DetalleVenta> resultado=new ArrayList<>();resultado.add(primera);
        if(lineas!=null)for(var l:lineas)if(l!=null && !l.vacia())resultado.add(new DetalleVenta(null,null,l.idProducto,l.cantidad,null,null));return resultado;
    }
}
