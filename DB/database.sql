-- Script de creacion de base de datos para Level Up Gamer (MySQL 8.0) - V0.5.x
CREATE DATABASE IF NOT EXISTS level_up_gamer;
USE level_up_gamer;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS tipo_usuario;
DROP TABLE IF EXISTS producto;
SET FOREIGN_KEY_CHECKS = 1;

-- Tabla: tipo_usuario
CREATE TABLE IF NOT EXISTS tipo_usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario_id INT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

-- Tabla: producto
CREATE TABLE IF NOT EXISTS producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo DOUBLE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100),
    descripcion TEXT,
    precio DOUBLE NOT NULL,
    cantidad INT NOT NULL,
    imagenUrl VARCHAR(255),
    imagenLocal VARCHAR(100),
    INDEX (codigo)
);

-- Datos iniciales
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Administrador', 'Acceso total al sistema');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Cliente', 'Acceso para realizar compras');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Supervisor', 'Gestion de inventario y pedidos');

INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id) VALUES ('admin', 'admin123', 'admin@example.com', 1);
INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id) VALUES ('usuario1', 'pass123', 'user1@example.com', 2);

INSERT INTO producto (codigo, nombre, categoria, descripcion, precio, cantidad, imagenLocal) VALUES
(1001, 'Teclado Mecanico RGB', 'Perifericos', 'Teclado mecanico con interruptores blue y retroiluminacion RGB.', 59.99, 15, 'i1001'),
(1002, 'Mouse Gamer Optrical', 'Perifericos', 'Mouse ergonomico de 16000 DPI con 6 botones programables.', 35.50, 25, 'i1002'),
(2001, 'Monitor 4K 144Hz', 'Monitores', 'Monitor de 27 pulgadas con resolucion 4K y tasa de refresco de 144Hz.', 399.00, 10, 'i2001'),
(3001, 'Headset Surround 7.1', 'Audio', 'Auriculares con sonido envolvente 7.1 y microfono con cancelacion de ruido.', 75.00, 20, 'i3001'),
(3002, 'Silla Gamer Ergonomica', 'Muebles', 'Silla ajustable con soporte lumbar y reposacabezas.', 189.99, 8, 'i3002'),
(4001, 'Tarjeta de Video RTX 4070', 'Componentes', 'Tarjeta grafica de ultima generacion para juegos exigentes.', 649.99, 5, 'i4001');
