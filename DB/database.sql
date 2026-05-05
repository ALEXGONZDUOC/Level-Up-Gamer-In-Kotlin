-- Script de creación de base de datos para Level Up Gamer (MySQL 8.0)

CREATE DATABASE IF NOT EXISTS level_up_gamer;
USE level_up_gamer;

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

-- Tabla: clientes
CREATE TABLE IF NOT EXISTS clientes (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
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

-- Tabla: pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    ClienteId INT NOT NULL,
    FechaPedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado VARCHAR(50),
    Total DOUBLE NOT NULL,
    FOREIGN KEY (ClienteId) REFERENCES clientes(Id)
);

-- Tabla: detalle_pedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Pedido_Id INT NOT NULL,
    Producto_Id INT NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DOUBLE NOT NULL,
    FOREIGN KEY (Pedido_Id) REFERENCES pedidos(Id),
    FOREIGN KEY (Producto_Id) REFERENCES producto(id)
);

-- Tabla: reseñas
CREATE TABLE IF NOT EXISTS reseñas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    producto_id INT NOT NULL,
    calificacion INT CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(Id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos iniciales
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Administrador', 'Acceso total al sistema');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Cliente', 'Acceso para realizar compras');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Supervisor', 'Gestión de inventario y pedidos');