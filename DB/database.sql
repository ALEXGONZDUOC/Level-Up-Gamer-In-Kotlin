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
-- Periféricos
(1001, 'Mouse Gamer Pro RGB', 'Periféricos', 'Sensor óptico de 16.000 DPI', 24990, 50, '', 'static/images/i1001.jpg', 10),
(1002, 'Teclado Mecánico Pro', 'Periféricos', 'Interruptores Blue, RGB', 45000, 30, '', 'static/images/i1002.jpg', 5),
(1003, 'Audífonos Gamer 7.1 Wireless', 'Periféricos', 'Sonido envolvente 7.1, micrófono con cancelación de ruido y batería de 20 horas.', 69990, 25, '', 'static/images/i1003.jpg', 4),
(1004, 'Mousepad XL Extended RGB', 'Periféricos', 'Superficie de tela optimizada para velocidad y control con bordes iluminados.', 19990, 40, '', 'static/images/i1004.jpg', 15),
(1005, 'Micrófono Condensador USB', 'Periféricos', 'Ideal para streaming y podcasting, patrón polar cardioide con trípode incluido.', 49900, 15, '', 'static/images/i1005.jpg', 3),
(1006, 'Cámara Web 1080p 60fps', 'Periféricos', 'Enfoque automático, corrección de luz automática y micrófono estéreo integrado.', 54990, 20, '', 'static/images/i1006.jpg', 6),

-- Monitores
(2001, 'Monitor 4K 144Hz', 'Monitores', 'Monitor de 27 pulgadas con resolución 4K y tasa de refresco de 144Hz.', 399000, 10, '', 'static/images/i2001.jpg', 2),
(2002, 'Monitor Curvo 24" 165Hz', 'Monitores', 'Panel VA, resolución Full HD, 1ms de respuesta y tecnología FreeSync.', 159900, 12, '', 'static/images/i2002.jpg', 5),
(2003, 'Monitor Ultrawide 34" Pro', 'Monitores', 'Resolución WQHD, tasa de refresco de 100Hz, ideal para productividad y multitasking.', 429900, 8, '', 'static/images/i2003.jpg', 1),

-- Componentes Internos
(3001, 'Procesador Intel Core i7-13700K', 'Componentes', '16 núcleos y 24 hilos, frecuencia máxima de hasta 5.4 GHz, zócalo LGA1700.', 389900, 15, '', 'static/images/i3001.jpg', 4),
(3002, 'Procesador AMD Ryzen 7 7800X3D', 'Componentes', '8 núcleos con tecnología 3D V-Cache, el rey indiscutido para gaming.', 419900, 10, '', 'static/images/i3002.jpg', 8),
(3003, 'Tarjeta de Video RTX 4070 Ti 12GB', 'Componentes', 'Arquitectura Ada Lovelace, trazado de rayos de 3ra gen y DLSS 3.', 849900, 7, '', 'static/images/i3003.jpg', 3),
(3004, 'Memoria RAM DDR5 32GB (2x16GB) 6000MHz', 'Componentes', 'Kit de alta velocidad con disipador de aluminio e iluminación RGB controlable.', 129900, 30, '', 'static/images/i3004.jpg', 12),
(3005, 'Memoria RAM DDR4 16GB (2x8GB) 3200MHz', 'Componentes', 'Kit clásico para actualizaciones, latencia CL16, excelente compatibilidad.', 45900, 50, '', 'static/images/i3005.jpg', 22),
(3006, 'Almacenamiento SSD M.2 NVMe 1TB PCIe 4.0', 'Componentes', 'Velocidades de lectura de hasta 7000 MB/s para cargas ultra rápidas.', 79990, 35, '', 'static/images/i3006.jpg', 18),
(3007, 'Disco Duro Externo 2TB USB 3.0', 'Componentes', 'Almacenamiento portátil y seguro, compatible con PC, Mac y consolas.', 64990, 25, '', 'static/images/i3007.jpg', 9),
(3008, 'Fuente de Poder 850W 80+ Gold Modular', 'Componentes', 'Condensadores japoneses, ventilador silencioso y cables 100% modulares.', 119900, 14, '', 'static/images/i3008.jpg', 5),
(3009, 'Refrigeración Líquida 240mm RGB', 'Componentes', 'Radiador doble, dos ventiladores PWM de 120mm y bomba de alta eficiencia.', 89900, 18, '', 'static/images/i3009.jpg', 4),
(3010, 'Gabinete Mid-Tower Vidrio Templado', 'Componentes', 'Panel lateral transparente, incluye 3 ventiladores ARGB frontales.', 69900, 20, '', 'static/images/i3010.jpg', 7),

-- Consolas y Audio
(4001, 'Consola PlayStation 5 Slim', 'Consolas', 'Edición estándar con lector de discos, almacenamiento SSD de 1TB y control DualSense.', 549900, 10, '', 'static/images/i4001.jpg', 3),
(4002, 'Control Inalámbrico Xbox Series X/S', 'Consolas', 'Diseño ergonómico en color negro, conectividad Bluetooth para PC y consola.', 59990, 25, '', 'static/images/i4002.jpg', 11),
(4003, 'Parlantes Gamer Bluetooth 2.0', 'Audio', 'Sonido estéreo nítido, alimentación USB y luces dinámicas sincronizadas.', 29990, 30, '', 'static/images/i4003.jpg', 14),
(4004, 'Consola Nintendo Switch OLED', 'Consolas', 'Pantalla OLED de 7 pulgadas, colores intensos, contraste definido y 64GB de almacenamiento.', 349900, 15, '', 'static/images/i4004.jpg', 9),

-- Muebles
(5001, 'Silla Gamer Ergonómica Premium', 'Muebles', 'Tapizado en cuero sintético, cojines lumbar y cervical, reclinable 180°.', 149900, 15, '', 'static/images/i5001.jpg', 6),

-- Juegos (Nintendo y PlayStation)
(6001, 'The Legend of Zelda: Tears of the Kingdom', 'Consolas y Juegos', 'Aventura épica en el reino de Hyrule, explora los cielos y descubre nuevas habilidades.', 64990, 15, '', 'static/images/i6001.jpg', 8),
(6002, 'Super Mario Bros. Wonder', 'Consolas y Juegos', 'El clásico juego de plataformas en 2D con un giro mágico y efectos sorprendentes.', 54990, 20, '', 'static/images/i6002.jpg', 12),
(6003, 'Mario Kart 8 Deluxe', 'Consolas y Juegos', 'Carreras llenas de diversión y adrenalina con todos tus personajes favoritos de Nintendo.', 52990, 30, '', 'static/images/i6003.jpg', 25),
(6004, 'Super Smash Bros. Ultimate', 'Consolas y Juegos', 'El juego de lucha definitivo con el plantel de personajes más grande de la historia.', 59990, 18, '', 'static/images/i6004.jpg', 14),
(6005, 'Pokémon Scarlet', 'Consolas y Juegos', 'Explora un mundo abierto lleno de Pokémon en la vasta región de Paldea.', 54990, 12, '', 'static/images/i6005.jpg', 6),
(6006, 'Marvel’s Spider-Man 2', 'Consolas y Juegos', 'Balancéate por Nueva York como Peter Parker y Miles Morales enfrentando a Venom.', 69990, 15, '', 'static/images/i6006.jpg', 9),
(6007, 'God of War Ragnarök', 'Consolas y Juegos', 'Kratos y Atreus emprenden un viaje mítico a través de los nueve reinos.', 59990, 10, '', 'static/images/i6007.jpg', 7),
(6008, 'Elden Ring', 'Consolas y Juegos', 'Un RPG de acción y fantasía oscura en un mundo abierto masivo y desafiante.', 49990, 22, '', 'static/images/i6008.jpg', 19),
(6009, 'Grand Theft Auto V', 'Consolas y Juegos', 'Disfruta de la mítica historia de Los Santos optimizada para la nueva generación.', 29990, 25, '', 'static/images/i6009.jpg', 30),
(6010, 'EA SPORTS FC 24', 'Consolas y Juegos', 'La experiencia de fútbol más realista del mundo con más de 19.000 jugadores.', 44990, 40, '', 'static/images/i6010.jpg', 45);

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
select *from producto;

