-- ======================================================
-- SCRIPT DE INICIALIZACIÓN REPARADO: LEVEL UP GAMER - V0.8
-- FORMATO: Pesos Chilenos (CLP) - Novedad: Pedidos Transaccionales y Seguridad
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

-- 2. Tabla de Usuarios (Actualizada con Verificación)
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    tipo_usuario_id INT NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    verificado TINYINT(1) DEFAULT 0,
    codigo_auth VARCHAR(6),
    fecha_creacion DATE NOT NULL,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
) ENGINE=InnoDB;

INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id, activo, verificado, fecha_creacion) VALUES
('admin', 'admin123', 'admin@example.com', 1, 1, 1, CURDATE()),
('super', 'super123', 'super@example.com', 2, 1, 1, CURDATE()),
('usuario1', 'pass123', 'user1@example.com', 3, 1, 1, CURDATE());

-- 3. Tabla de Direcciones
CREATE TABLE direcciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre_etiqueta VARCHAR(50),
    calle VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    referencias TEXT,
    latitud DOUBLE DEFAULT 0.0,
    longitud DOUBLE DEFAULT 0.0,
    es_principal TINYINT(1) DEFAULT 0,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO direcciones (usuario_id, nombre_etiqueta, calle, ciudad, es_principal) VALUES
(3, 'Casa', 'Av. Siempreviva 742', 'Santiago', 1);

-- 4. Tabla de Productos
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo DOUBLE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    categoria VARCHAR(100),
    descripcion TEXT,
    precio INT NOT NULL,
    cantidad INT DEFAULT 0,
    imagenUrl VARCHAR(500) DEFAULT '',
    imagenLocal VARCHAR(100) DEFAULT 'product_placeholder',
    total_vendido INT DEFAULT 0,
    UNIQUE KEY (codigo)
) ENGINE=InnoDB;

INSERT INTO producto (codigo, nombre, categoria, descripcion, precio, cantidad, imagenUrl, imagenLocal, total_vendido) VALUES
(1001, 'Teclado Mecánico RGB', 'Periféricos', 'Teclado mecánico con interruptores blue y retroiluminación RGB.', 59990, 15, 'static/images/i1001.jpg', 'i1001', 5),
(1002, 'Mouse Gamer Optical', 'Periféricos', 'Mouse ergonómico de 16000 DPI con 6 botones programables.', 35500, 25, 'static/images/i1002.jpg', 'i1002', 12),
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

-- Registro de prueba inicial
INSERT INTO pedidos (usuario_id, direccion, total) VALUES (3, 'Av. Siempreviva 742, Santiago', 59990);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (1, 1, 1, 59990);

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================
