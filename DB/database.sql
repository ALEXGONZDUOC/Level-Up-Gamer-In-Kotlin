-- Script de creación de base de datos para Level Up Gamer (MySQL 8.0)
-- VERSION 0.2: Refleja fielmente el esquema inicial de Room (V0.1)

CREATE DATABASE IF NOT EXISTS level_up_gamer;
USE level_up_gamer;

CREATE TABLE tipo_usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario_id INT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion VARCHAR(50),
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

CREATE TABLE clientes (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo DOUBLE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DOUBLE NOT NULL,
    cantidad INT NOT NULL,
    imagenUrl VARCHAR(255) DEFAULT '',
    imagenLocal VARCHAR(100) DEFAULT 'product_placeholder'
);

CREATE TABLE pedidos (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    ClienteId INT NOT NULL,
    FechaPedido VARCHAR(50),
    Estado VARCHAR(50),
    Total DOUBLE NOT NULL,
    FOREIGN KEY (ClienteId) REFERENCES clientes(Id)
);

CREATE TABLE detalle_pedido (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Pedido_Id INT NOT NULL,
    Producto_Id INT NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DOUBLE NOT NULL,
    FOREIGN KEY (Pedido_Id) REFERENCES pedidos(Id),
    FOREIGN KEY (Producto_Id) REFERENCES producto(id)
);

CREATE TABLE reseñas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    producto_id INT NOT NULL,
    calificacion INT NOT NULL,
    comentario TEXT,
    fecha VARCHAR(50),
    FOREIGN KEY (cliente_id) REFERENCES clientes(Id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos iniciales V0.2
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Administrador', 'Usuario con permisos completos');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Cliente', 'Usuario cliente estándar');
