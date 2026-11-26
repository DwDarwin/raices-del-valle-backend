-- Eliminación lógica de producto
-- Ejecutar UNA vez en cada base de datos que ya exista (local y la compartida del despliegue).
-- Los productos existentes quedan activos por el DEFAULT TRUE.
ALTER TABLE producto ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

-- Verificación
SELECT id, nombre, estado, activo FROM producto;


