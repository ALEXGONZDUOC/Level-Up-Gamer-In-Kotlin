-- ======================================================
-- SCRIPT DE INICIALIZACIÓN: LEVEL UP GAMER - V0.5.2
-- FORMATO: Pesos Chilenos (CLP) - Base Refinada
-- ======================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS level_up_gamer;
CREATE DATABASE level_up_gamer;
USE level_up_gamer;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Roles del Sistema
CREATE TABLE tipo_usuario (
    id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

INSERT INTO tipo_usuario (id, nombre, descripcion) VALUES
(1, 'Admin', 'Administración total del sistema'),
(2, 'Supervisor', 'Acceso a reportería y estadísticas'),
(3, 'Usuario', 'Cliente final de la tienda');

-- 2. Tabla de Usuarios
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    tipo_usuario_id INT NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion DATE NOT NULL,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
) ENGINE=InnoDB;

INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id, activo, fecha_creacion) VALUES
('admin', 'admin123', 'admin@example.com', 1, 1, CURDATE()),
('super', 'super123', 'super@example.com', 2, 1, CURDATE()),
('usuario1', 'pass123', 'user1@example.com', 3, 1, CURDATE());

-- 3. Tabla de Productos
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo DOUBLE UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(100),
    descripcion TEXT,
    precio INT NOT NULL,
    cantidad INT DEFAULT 0,
    imagenUrl VARCHAR(500) DEFAULT '',
    imagenLocal VARCHAR(100) DEFAULT 'product_placeholder'
) ENGINE=InnoDB;

INSERT INTO producto (codigo, nombre, categoria, descripcion, precio, cantidad, imagenLocal) VALUES
(1001, 'Teclado Mecánico RGB', 'Periféricos', 'Teclado mecánico con interruptores blue y retroiluminación RGB.', 59990, 15, 'i1001'),
(1002, 'Mouse Gamer Optical', 'Periféricos', 'Mouse ergonómico de 16000 DPI con 6 botones programables.', 35500, 25, 'i1002'),
(2001, 'Monitor 4K 144Hz', 'Monitores', 'Monitor de 27 pulgadas con resolución 4K y tasa de refresco de 144Hz.', 399000, 10, 'i2001'),
(3001, 'Headset Surround 7.1', 'Audio', 'Auriculares con sonido envolvente 7.1.', 75000, 20, 'i3001');

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================
