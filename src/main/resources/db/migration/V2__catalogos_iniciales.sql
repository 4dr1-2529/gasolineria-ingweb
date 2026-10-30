INSERT INTO rol (codigo,nombre,descripcion,estado) VALUES
('ADMINISTRADOR','Administrador','Catálogos, compras, personal, caja y supervisión','Activo'),
('OPERADOR','Operador','Consulta y ajustes de inventario autorizados','Activo'),
('VENDEDOR','Vendedor','Ventas y consulta operativa permitida','Activo');
INSERT INTO concepto_movimiento (codigo,nombre,tipo,estado) VALUES
('CE01','Venta de combustible','INGRESO','Activo'),
('CE02','Compra de combustible','EGRESO','Activo');
