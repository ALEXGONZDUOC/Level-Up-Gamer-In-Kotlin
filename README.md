# Level Up Gamer - V0.4 (Backend Launch)

## 📝 Descripción
Lanzamiento del backend centralizado utilizando FastAPI para desacoplar los datos de la aplicación móvil.

## 🚀 Novedades
- **API REST**: Endpoints iniciales de Login, Usuarios y Productos.
- **MySQL Auth**: Script de creación de usuario `gamer` con permisos específicos.
- **Backend**: Servidor ligero en Python (FastAPI).

## 🛠️ Instalación

### 1. Base de Datos
Ejecutar:
- `DB/00_setup_user.sql`
- `DB/database.sql`

### 2. Backend (API)
```bash
cd api
pip install fastapi uvicorn pymysql
python main.py