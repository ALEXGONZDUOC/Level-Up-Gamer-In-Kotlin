-- Configuración de Usuario y Permisos para MySQL 8.0
-- Ejecutar como usuario 'root'

CREATE USER IF NOT EXISTS 'gamer'@'localhost' IDENTIFIED BY 'password123';
CREATE USER IF NOT EXISTS 'gamer'@'%' IDENTIFIED BY 'password123';

GRANT ALL PRIVILEGES ON level_up_gamer.* TO 'gamer'@'localhost';
GRANT ALL PRIVILEGES ON level_up_gamer.* TO 'gamer'@'%';

FLUSH PRIVILEGES;
