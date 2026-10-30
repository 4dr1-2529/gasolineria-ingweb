-- Respalda la unicidad de nombres normalizados que ya aplica el servicio de conceptos.
ALTER TABLE concepto_movimiento ADD CONSTRAINT uq_concepto_nombre UNIQUE(nombre);
