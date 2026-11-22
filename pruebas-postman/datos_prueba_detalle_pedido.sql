-- Datos para probar detalle_pedido en Postman.
-- Ejecutar en Workbench DESPUÉS de "Registrar usuario" y "Crear producto" en Postman.
USE bd_raices_del_valle;

-- Pedido de prueba del usuario registrado (aún no hay endpoint de pedidos)
INSERT INTO pedido (codigo_pedido, id_usuario, subtotal, descuento, total)
SELECT 'RDV-PRUEBA-001', id, 0, 0, 0
FROM usuario
WHERE correo = 'cliente.prueba@raices.com';

-- Ids que van en las variables idPedido e idProducto de la colección
SELECT id AS idPedido FROM pedido WHERE codigo_pedido = 'RDV-PRUEBA-001';
SELECT id AS idProducto FROM producto WHERE nombre = 'Papa criolla';

-- Para ver lo que va guardando Postman
-- SELECT * FROM detalle_pedido;
