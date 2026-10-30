CREATE INDEX ix_compra_fecha_proveedor ON compra(fecha_hora,id_proveedor,id_usuario);
CREATE INDEX ix_venta_fecha_usuario ON venta(fecha_hora,id_usuario);
CREATE INDEX ix_inventario_fecha_producto ON movimiento_inventario(fecha_hora,id_producto,tipo);
CREATE INDEX ix_caja_tipo_fecha ON movimiento_caja(id_caja,tipo,fecha_hora);
CREATE INDEX ix_asistencia_fecha_estado ON asistencia(fecha,estado);
