-- Datos ficticios del corte académico. Solo se ejecutan con el perfil demo y sin cuentas existentes.
INSERT INTO categoria VALUES (1,'Gasolinas','Combustibles para motores a gasolina','Activo','2026-09-10 07:00:00'),(2,'Diésel','Combustibles para motores diésel','Activo','2026-09-10 07:00:00');
INSERT INTO producto VALUES (1,'PR01',1,'Gasolina Regular','L',5,1990,'Activo'),(2,'PR02',1,'Gasolina Premium','L',6,980,'Activo'),(3,'PR03',2,'Diésel','L',4,3950,'Activo');
INSERT INTO proveedor VALUES (1,'00000000001','Petroandes S.A.',NULL,NULL,NULL,NULL,'Activo');
INSERT INTO empleado VALUES (1,'00000001','Ana','Torres','Operadora','000 000 001',NULL,'Activo'),(2,'00000002','Luis','Rojas','Vendedor','000 000 002',NULL,'Activo'),(3,'00000003','Elena','Díaz','Administradora','000 000 003',NULL,'Activo');
INSERT INTO usuario VALUES (1,1,2,'atorres','${passwordHash}','Activo','2026-09-10 07:00:00',NULL),(2,2,3,'lrojas','${passwordHash}','Activo','2026-09-10 07:00:00',NULL),(3,3,1,'ediaz','${passwordHash}','Activo','2026-09-10 07:00:00',NULL);
INSERT INTO compra VALUES (1,'C001',1,3,'2026-09-10 07:00:00',1350,'Confirmada',NULL);
INSERT INTO detalle_compra VALUES (1,1,1,100,4.50,450),(2,1,2,100,5.40,540),(3,1,3,100,3.60,360);
INSERT INTO venta VALUES (1,'V001',2,'2026-09-10 09:00:00',50,'Confirmada'),(2,'V002',2,'2026-09-10 10:00:00',120,'Confirmada'),(3,'V003',2,'2026-09-10 11:00:00',200,'Confirmada');
INSERT INTO detalle_venta VALUES (1,1,1,10,5,50),(2,2,2,20,6,120),(3,3,3,50,4,200);
INSERT INTO movimiento_inventario VALUES (1,1,3,1,NULL,'ENTRADA',100,'2026-09-10 07:00:00','Compra C001'),(2,2,3,2,NULL,'ENTRADA',100,'2026-09-10 07:00:00','Compra C001'),(3,3,3,3,NULL,'ENTRADA',100,'2026-09-10 07:00:00','Compra C001'),(4,1,2,NULL,1,'SALIDA',10,'2026-09-10 09:00:00','Venta V001'),(5,2,2,NULL,2,'SALIDA',20,'2026-09-10 10:00:00','Venta V002'),(6,3,2,NULL,3,'SALIDA',50,'2026-09-10 11:00:00','Venta V003');
INSERT INTO caja_diaria VALUES (1,'2026-09-10',4410,3430,'CERRADA',3,3,'2026-09-10 07:00:00','2026-09-10 17:00:00');
INSERT INTO caja_diaria SELECT 2,CURRENT_DATE(),0,NULL,'ABIERTA',3,NULL,NOW(),NULL WHERE CURRENT_DATE()<>'2026-09-10';
INSERT INTO movimiento_caja VALUES (1,'MC001',1,2,3,1,NULL,'EGRESO',1350,'Compra C001','2026-09-10 07:00:00'),(3,'MC003',1,1,2,NULL,1,'INGRESO',50,'Venta V001','2026-09-10 09:00:00'),(4,'MC004',1,1,2,NULL,2,'INGRESO',120,'Venta V002','2026-09-10 10:00:00'),(5,'MC005',1,1,2,NULL,3,'INGRESO',200,'Venta V003','2026-09-10 11:00:00');
INSERT INTO asistencia VALUES (1,1,'2026-09-10','08:00:00','17:00:00','PRESENTE','Jornada completa'),(2,1,'2026-09-09','07:58:00','17:00:00','PRESENTE','Jornada completa'),(4,2,'2026-09-10','08:00:00','17:00:00','PRESENTE','Jornada completa'),(5,3,'2026-09-10','08:15:00','17:00:00','PRESENTE','Ingreso posterior a las 08:00');
