-- Script de creación de base de datos para Level Up Gamer (MySQL 8.0)
<<<<<<< HEAD
CREATE DATABASE IF NOT EXISTS level_up_gamer;
USE level_up_gamer;

-- Tabla: tipo_usuario
CREATE TABLE IF NOT EXISTS tipo_usuario (
=======
-- VERSION 0.2: Refleja fielmente el esquema inicial de Room (V0.1)

CREATE DATABASE IF NOT EXISTS level_up_gamer;
USE level_up_gamer;

CREATE TABLE tipo_usuario (
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

<<<<<<< HEAD
-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
=======
CREATE TABLE usuario (
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario_id INT,
    activo BOOLEAN DEFAULT TRUE,
<<<<<<< HEAD
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

-- Tabla: clientes
CREATE TABLE IF NOT EXISTS clientes (
=======
    fecha_creacion VARCHAR(50),
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

CREATE TABLE clientes (
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    Id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

<<<<<<< HEAD
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
=======
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
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    Estado VARCHAR(50),
    Total DOUBLE NOT NULL,
    FOREIGN KEY (ClienteId) REFERENCES clientes(Id)
);

<<<<<<< HEAD
-- Tabla: detalle_pedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
=======
CREATE TABLE detalle_pedido (
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Pedido_Id INT NOT NULL,
    Producto_Id INT NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DOUBLE NOT NULL,
    FOREIGN KEY (Pedido_Id) REFERENCES pedidos(Id),
    FOREIGN KEY (Producto_Id) REFERENCES producto(id)
);

<<<<<<< HEAD
-- Tabla: reseñas
CREATE TABLE IF NOT EXISTS reseñas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    producto_id INT NOT NULL,
    calificacion INT CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
=======
CREATE TABLE reseñas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    producto_id INT NOT NULL,
    calificacion INT NOT NULL,
    comentario TEXT,
    fecha VARCHAR(50),
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
    FOREIGN KEY (cliente_id) REFERENCES clientes(Id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

<<<<<<< HEAD
-- Datos iniciales sugeridos
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Administrador', 'Acceso total al sistema');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Cliente', 'Acceso para realizar compras');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Supervisor', 'Gestión de inventario y pedidos');
=======
-- Datos iniciales V0.2
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Administrador', 'Usuario con permisos completos');
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Cliente', 'Usuario cliente estándar');
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
