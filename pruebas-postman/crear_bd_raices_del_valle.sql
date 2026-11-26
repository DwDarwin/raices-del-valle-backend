-- Script del equipo: crea la base y las 4 tablas de Raíces del Valle.
-- Ejecutar una sola vez en MySQL Workbench (botón del rayo).
CREATE DATABASE IF NOT EXISTS bd_raices_del_valle;
USE bd_raices_del_valle;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    documento VARCHAR(45) NOT NULL UNIQUE,
    telefono VARCHAR(15) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(40) NOT NULL,
    productor VARCHAR(120) NOT NULL,
    precio DECIMAL(13, 2) NOT NULL,
    unidad_venta VARCHAR(60) NOT NULL,
    stock INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    imagen LONGTEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_pedido VARCHAR(50) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    subtotal DECIMAL(13, 2) NOT NULL,
    descuento DECIMAL(13, 2) NOT NULL DEFAULT 0,
    total DECIMAL(13, 2) NOT NULL,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(13, 2) NOT NULL,
    subtotal DECIMAL(13, 2) NOT NULL,
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id)
);
