<<<<<<< HEAD
# Level Up Gamer - V0.3 (Clean Code)

## 📝 Descripción
Fase de limpieza de código para preparar la aplicación para el consumo de servicios externos.

## 🚀 Novedades
- **Eliminación de Mocks**: Se removieron todos los datos estáticos de `LevelUpDatabase.kt`.
- **Simplificación**: Se eliminó la funcionalidad de reseñas para optimizar el núcleo de la app.
- **Preparación de DAOs**: Ajuste de interfaces de Room para futura migración.

## 🛠️ Instalación
- No requiere configuración adicional. La base de datos local estará vacía hasta que se registren nuevos usuarios.

---
*Nota: Esta versión es el "punto cero" para la integración con la API.*
=======
# Level Up Gamer - V0.2 (Database Schema)

## 📝 Descripción
Primera transición hacia una arquitectura cliente-servidor mediante la definición del esquema de base de datos relacional en MySQL 8.0.

## 🚀 Novedades
- **Esquema SQL**: Creación de tablas para Usuarios, Productos, Pedidos y Detalles.
- **Relaciones**: Implementación de llaves foráneas para integridad de datos.
- **Scripts**: Inclusión de scripts de inicialización de datos.

## 🛠️ Instalación
1. Abrir MySQL Workbench.
2. Ejecutar el script ubicado en `DB/database.sql`.
3. Validar la creación de la base de datos `level_up_gamer`.

---
*Nota: En esta versión, la App Android aún utiliza Room. El esquema SQL es preparatorio para la API.*
>>>>>>> cdf1b3b9cab788d203973195dc8a89bb54f369c8
