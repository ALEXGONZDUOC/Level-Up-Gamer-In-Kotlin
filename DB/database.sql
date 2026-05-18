-- ======================================================
-- SCRIPT DE INICIALIZACIÓN: LEVEL UP GAMER - V0.9 (FINAL)
-- FORMATO: Pesos Chilenos (CLP) - Reflejo de pre_finalVersion
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
('admin', 'LvlUpGamer_2026!XP', 'levelup.gamer.2002@gmail.com', 1, 1, CURDATE()),
('super', 'super123', 'super@example.com', 2, 1, CURDATE()),
('usuario1', 'pass123', 'user1@example.com', 3, 1, CURDATE());

-- 3. Tabla de Direcciones
CREATE TABLE direcciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre_etiqueta VARCHAR(50),
    calle VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    referencias TEXT,
    latitud DOUBLE,
    longitud DOUBLE,
    es_principal TINYINT(1) DEFAULT 0,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO direcciones (usuario_id, nombre_etiqueta, calle, ciudad, es_principal) VALUES
(3, 'Casa', 'Av. Siempreviva 742', 'Santiago', 1);

-- 4. Tabla de Productos
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo DOUBLE UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(100),
    descripcion TEXT,
    precio INT NOT NULL,
    cantidad INT DEFAULT 0,
    imagenUrl VARCHAR(500) DEFAULT '',
    imagenLocal VARCHAR(100) DEFAULT 'product_placeholder',
    total_vendido INT DEFAULT 0
) ENGINE=InnoDB;

INSERT INTO producto (codigo, nombre, categoria, descripcion, precio, cantidad, imagenUrl, imagenLocal, total_vendido) VALUES
(1001, 'Mouse Gamer Pro RGB', 'Periféricos', 'Sensor óptico de 16.000 DPI', 24990, 50, 'static/images/i1001.jpg', 'i1001', 10),
(1002, 'Teclado Mecánico Pro', 'Periféricos', 'Interruptores Blue, RGB', 45000, 30, 'static/images/i1002.jpg', 'i1002', 5),
(2001, 'Monitor 4K 144Hz', 'Monitores', 'Monitor de 27 pulgadas con resolución 4K y tasa de refresco de 144Hz.', 399000, 10, 'static/images/i2001.jpg', 'i2001', 2);

-- 5. Pedidos y Detalles
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    total INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id)
) ENGINE=InnoDB;

INSERT INTO pedidos (usuario_id, direccion, total) VALUES (3, 'Av. Siempreviva 742, Santiago', 69990);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (1, 1, 1, 24990), (1, 1002, 1, 45000);

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================
