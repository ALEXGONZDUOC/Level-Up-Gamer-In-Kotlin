-- Script de actualización para V0.6: Inclusión de Supervisor
USE level_up_gamer;

-- Agregar campo para estadísticas si no existe
ALTER TABLE producto ADD COLUMN total_vendido INT DEFAULT 0;

-- Asegurar rol de supervisor
INSERT INTO tipo_usuario (nombre, descripcion) VALUES ('Supervisor', 'Gestión de inventario y estadísticas');
