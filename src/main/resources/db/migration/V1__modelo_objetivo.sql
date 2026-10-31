-- Modelo objetivo del Word corregido, sección 5.10.2 (30/10/2026).
-- Migración activa de Flyway; Hibernate valida el esquema.
-- Compatible con MySQL 8 y MariaDB de XAMPP. Mantiene 15 tablas,
-- 109 columnas de negocio y 22 FK.
SET time_zone = '-05:00';

CREATE TABLE categoria (
  id_categoria BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  descripcion VARCHAR(200) NULL,
  estado VARCHAR(10) NOT NULL,
  fecha_creacion DATETIME NOT NULL,
  CONSTRAINT uq_categoria_nombre UNIQUE (nombre),
  CONSTRAINT ck_categoria_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE producto (
  id_producto BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_producto VARCHAR(10) NOT NULL,
  id_categoria BIGINT NOT NULL,
  nombre VARCHAR(60) NOT NULL,
  unidad_medida VARCHAR(10) NOT NULL,
  precio_venta DECIMAL(12,2) NOT NULL,
  stock_actual DECIMAL(12,3) NOT NULL,
  estado VARCHAR(10) NOT NULL,
  CONSTRAINT uq_producto_codigo_producto UNIQUE (codigo_producto),
  CONSTRAINT uq_producto_categoria_nombre UNIQUE (id_categoria,nombre),
  CONSTRAINT ck_producto_1 CHECK (precio_venta > 0),
  CONSTRAINT ck_producto_2 CHECK (stock_actual >= 0),
  CONSTRAINT ck_producto_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE proveedor (
  id_proveedor BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  ruc CHAR(11) NOT NULL,
  razon_social VARCHAR(120) NOT NULL,
  contacto VARCHAR(80) NULL,
  telefono VARCHAR(15) NULL,
  correo VARCHAR(120) NULL,
  direccion VARCHAR(200) NULL,
  estado VARCHAR(10) NOT NULL,
  CONSTRAINT uq_proveedor_ruc UNIQUE (ruc),
  CONSTRAINT ck_proveedor_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE compra (
  id_compra BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_compra VARCHAR(12) NOT NULL,
  id_proveedor BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  fecha_hora DATETIME NOT NULL,
  total DECIMAL(12,2) NOT NULL,
  estado VARCHAR(20) NOT NULL,
  observacion VARCHAR(200) NULL,
  CONSTRAINT uq_compra_codigo_compra UNIQUE (codigo_compra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE detalle_compra (
  id_detalle_compra BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_compra BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  cantidad_litros DECIMAL(12,3) NOT NULL,
  precio_compra DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT ck_detalle_compra_1 CHECK (cantidad_litros > 0),
  CONSTRAINT ck_detalle_compra_2 CHECK (precio_compra > 0),
  CONSTRAINT ck_detalle_compra_3 CHECK (subtotal > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE empleado (
  id_empleado BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  dni CHAR(8) NOT NULL,
  nombres VARCHAR(60) NOT NULL,
  apellidos VARCHAR(60) NOT NULL,
  cargo VARCHAR(40) NOT NULL,
  telefono VARCHAR(15) NOT NULL,
  fecha_ingreso DATE NULL,
  estado VARCHAR(10) NOT NULL,
  CONSTRAINT uq_empleado_dni UNIQUE (dni),
  CONSTRAINT ck_empleado_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE rol (
  id_rol BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(40) NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  estado VARCHAR(10) NOT NULL,
  CONSTRAINT uq_rol_codigo UNIQUE (codigo),
  CONSTRAINT uq_rol_nombre UNIQUE (nombre),
  CONSTRAINT ck_rol_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario (
  id_usuario BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_empleado BIGINT NOT NULL,
  id_rol BIGINT NOT NULL,
  username VARCHAR(30) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  estado VARCHAR(10) NOT NULL,
  fecha_creacion DATETIME NOT NULL,
  ultimo_acceso DATETIME NULL,
  CONSTRAINT uq_usuario_username UNIQUE (username),
  CONSTRAINT uq_usuario_id_empleado UNIQUE (id_empleado),
  CONSTRAINT ck_usuario_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE venta (
  id_venta BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_venta VARCHAR(12) NOT NULL,
  id_usuario BIGINT NOT NULL,
  fecha_hora DATETIME NOT NULL,
  total DECIMAL(12,2) NOT NULL,
  estado VARCHAR(20) NOT NULL,
  CONSTRAINT uq_venta_codigo_venta UNIQUE (codigo_venta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE detalle_venta (
  id_detalle_venta BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_venta BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  cantidad_litros DECIMAL(12,3) NOT NULL,
  precio_unitario DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT ck_detalle_venta_1 CHECK (cantidad_litros > 0),
  CONSTRAINT ck_detalle_venta_2 CHECK (precio_unitario > 0),
  CONSTRAINT ck_detalle_venta_3 CHECK (subtotal > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimiento_inventario (
  id_movimiento_inventario BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_producto BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  id_detalle_compra BIGINT NULL,
  id_detalle_venta BIGINT NULL,
  tipo VARCHAR(10) NOT NULL,
  cantidad_litros DECIMAL(12,3) NOT NULL,
  fecha_hora DATETIME NOT NULL,
  motivo VARCHAR(200) NOT NULL,
  CONSTRAINT uq_movimiento_inventario_id_detalle_compra UNIQUE (id_detalle_compra),
  CONSTRAINT uq_movimiento_inventario_id_detalle_venta UNIQUE (id_detalle_venta),
  CONSTRAINT ck_movimiento_inventario_1 CHECK (cantidad_litros > 0),
  CONSTRAINT ck_movimiento_inventario_2 CHECK (tipo IN ('ENTRADA','SALIDA')),
  CONSTRAINT ck_movimiento_inventario_3 CHECK (NOT (id_detalle_compra IS NOT NULL AND id_detalle_venta IS NOT NULL)),
  CONSTRAINT ck_movimiento_inventario_4 CHECK (id_detalle_compra IS NULL OR tipo = 'ENTRADA'),
  CONSTRAINT ck_movimiento_inventario_5 CHECK (id_detalle_venta IS NULL OR tipo = 'SALIDA')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE concepto_movimiento (
  id_concepto BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(10) NOT NULL,
  nombre VARCHAR(60) NOT NULL,
  tipo VARCHAR(10) NOT NULL,
  estado VARCHAR(10) NOT NULL,
  CONSTRAINT uq_concepto_movimiento_codigo UNIQUE (codigo),
  CONSTRAINT ck_concepto_movimiento_1 CHECK (tipo IN ('INGRESO','EGRESO')),
  CONSTRAINT ck_concepto_movimiento_estado CHECK (estado IN ('Activo','Inactivo'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE caja_diaria (
  id_caja BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  saldo_apertura DECIMAL(12,2) NOT NULL,
  saldo_cierre DECIMAL(12,2) NULL,
  estado VARCHAR(10) NOT NULL,
  id_usuario_apertura BIGINT NOT NULL,
  id_usuario_cierre BIGINT NULL,
  fecha_apertura DATETIME NOT NULL,
  fecha_cierre DATETIME NULL,
  CONSTRAINT uq_caja_diaria_fecha UNIQUE (fecha),
  CONSTRAINT ck_caja_diaria_1 CHECK (saldo_apertura >= 0),
  CONSTRAINT ck_caja_diaria_2 CHECK (estado IN ('ABIERTA','CERRADA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimiento_caja (
  id_movimiento_caja BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_movimiento VARCHAR(12) NOT NULL,
  id_caja BIGINT NOT NULL,
  id_concepto BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  id_compra BIGINT NULL,
  id_venta BIGINT NULL,
  tipo VARCHAR(10) NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  fecha_hora DATETIME NOT NULL,
  CONSTRAINT uq_movimiento_caja_codigo_movimiento UNIQUE (codigo_movimiento),
  CONSTRAINT uq_movimiento_caja_id_compra UNIQUE (id_compra),
  CONSTRAINT uq_movimiento_caja_id_venta UNIQUE (id_venta),
  CONSTRAINT ck_movimiento_caja_1 CHECK (monto > 0),
  CONSTRAINT ck_movimiento_caja_2 CHECK ((id_compra IS NOT NULL AND id_venta IS NULL AND tipo = 'EGRESO') OR (id_venta IS NOT NULL AND id_compra IS NULL AND tipo = 'INGRESO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE asistencia (
  id_asistencia BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_empleado BIGINT NOT NULL,
  fecha DATE NOT NULL,
  hora_entrada TIME NOT NULL,
  hora_salida TIME NULL,
  estado VARCHAR(10) NOT NULL,
  observacion VARCHAR(200) NULL,
  CONSTRAINT uq_asistencia_jornada UNIQUE (id_empleado,fecha),
  CONSTRAINT ck_asistencia_1 CHECK (hora_salida IS NULL OR hora_salida > hora_entrada),
  CONSTRAINT ck_asistencia_2 CHECK (estado IN ('PRESENTE','FALTA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE producto ADD CONSTRAINT fk_producto_id_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE compra ADD CONSTRAINT fk_compra_id_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedor (id_proveedor) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE compra ADD CONSTRAINT fk_compra_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE detalle_compra ADD CONSTRAINT fk_detalle_compra_id_compra FOREIGN KEY (id_compra) REFERENCES compra (id_compra) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE detalle_compra ADD CONSTRAINT fk_detalle_compra_id_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_id_empleado FOREIGN KEY (id_empleado) REFERENCES empleado (id_empleado) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_id_rol FOREIGN KEY (id_rol) REFERENCES rol (id_rol) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE venta ADD CONSTRAINT fk_venta_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE detalle_venta ADD CONSTRAINT fk_detalle_venta_id_venta FOREIGN KEY (id_venta) REFERENCES venta (id_venta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE detalle_venta ADD CONSTRAINT fk_detalle_venta_id_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_inventario ADD CONSTRAINT fk_movimiento_inventario_id_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_inventario ADD CONSTRAINT fk_movimiento_inventario_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_inventario ADD CONSTRAINT fk_movimiento_inventario_id_detalle_compra FOREIGN KEY (id_detalle_compra) REFERENCES detalle_compra (id_detalle_compra) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_inventario ADD CONSTRAINT fk_movimiento_inventario_id_detalle_venta FOREIGN KEY (id_detalle_venta) REFERENCES detalle_venta (id_detalle_venta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE caja_diaria ADD CONSTRAINT fk_caja_diaria_id_usuario_apertura FOREIGN KEY (id_usuario_apertura) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE caja_diaria ADD CONSTRAINT fk_caja_diaria_id_usuario_cierre FOREIGN KEY (id_usuario_cierre) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_caja ADD CONSTRAINT fk_movimiento_caja_id_caja FOREIGN KEY (id_caja) REFERENCES caja_diaria (id_caja) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_caja ADD CONSTRAINT fk_movimiento_caja_id_concepto FOREIGN KEY (id_concepto) REFERENCES concepto_movimiento (id_concepto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_caja ADD CONSTRAINT fk_movimiento_caja_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_caja ADD CONSTRAINT fk_movimiento_caja_id_compra FOREIGN KEY (id_compra) REFERENCES compra (id_compra) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE movimiento_caja ADD CONSTRAINT fk_movimiento_caja_id_venta FOREIGN KEY (id_venta) REFERENCES venta (id_venta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE asistencia ADD CONSTRAINT fk_asistencia_id_empleado FOREIGN KEY (id_empleado) REFERENCES empleado (id_empleado) ON DELETE RESTRICT ON UPDATE RESTRICT;
