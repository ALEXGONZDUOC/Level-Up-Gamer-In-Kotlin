# 🎮 Level Up Gamer - V1.0 (Official Release)

## 📝 Descripción
**Level Up Gamer** es un ecosistema de comercio electrónico diseñado específicamente para la comunidad gamer. El sistema integra una aplicación móvil nativa desarrollada en **Android (Jetpack Compose)** y un backend robusto con **FastAPI**, utilizando **MySQL** como motor de persistencia transaccional.

El proyecto destaca por su sistema de roles dinámico, gestión de inventario en tiempo real y reportes estadísticos avanzados para la toma de decisiones.

---

## 🚀 Tecnologías Utilizadas

### Frontend (Móvil)
*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose (Modern Declarative UI)
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Networking:** Retrofit 2 & OkHttp
*   **Imágenes:** Coil (Image Loading)
*   **Maps:** Google Maps Compose Utility
*   **Inyección de Dependencias:** ViewModel & StateFlow para reactividad.

### Backend (API)
*   **Framework:** FastAPI (Python 3.9+)
*   **Base de Datos:** MySQL 8.0
*   **ORM/Driver:** PyMySQL
*   **Seguridad:** Validación de roles y estados de cuenta.
*   **Notificaciones:** SMTP (Gmail) para confirmación automática de pedidos.

---

## 🔑 Sistema de Roles

El sistema adapta su interfaz y capacidades según el tipo de usuario logueado:

1.  **Administrador (ID: 1):** Control total de usuarios (activar/desactivar), gestión de catálogos y acceso al dashboard de gestión.
2.  **Supervisor (ID: 2):** Enfoque en métricas. Visualiza gráficos de ventas por día/semana/mes y rendimiento detallado por producto.
3.  **Usuario Final (ID: 3):** Experiencia de compra completa: catálogo interactivo, carrito de compras, gestión de múltiples direcciones y seguimiento de pedidos.

---

## 🛠️ Instalación y Configuración

### 1. Backend (API)
Navega a la carpeta `/api` y sigue estos pasos:
```bash
# Instalar dependencias
pip install fastapi uvicorn pymysql python-dotenv

# Configurar variables de entorno
# Crea un archivo .env basado en .env.example
GMAIL_USER="tu_correo@gmail.com"
GMAIL_PASS="tu_contraseña_de_aplicacion"

# Iniciar servidor
python main.py
```
*La API correrá por defecto en `http://localhost:3000`.*

### 2. Base de Datos
Importa el archivo `/DB/database.sql` en tu servidor MySQL para inicializar las tablas y los datos de prueba.

### 3. Aplicación Android
*   Abre el proyecto en **Android Studio**.
*   Asegúrate de configurar la IP de tu servidor en `RetrofitClient.kt`.
*   Sincroniza con Gradle y ejecuta en un emulador o dispositivo físico (Min SDK 27).

---

## 📊 Características Destacadas
*   **Persistencia Transaccional:** El sistema asegura que el stock se descuente solo si el pedido se guarda correctamente en la base de datos (Operaciones ACID).
*   **UX Gamer:** Interfaz personalizada con fuentes "Orbitron", gradientes oscuros y animaciones fluidas.
*   **Geolocalización:** Gestión de direcciones mediante coordenadas para futura integración logística.
*   **Reportes en Tiempo Real:** Los supervisores ven cambios inmediatos en las gráficas tras cada venta.

---

## 📄 Documentación Adicional
*   [Manual del Sistema](DOCUMENTACION_SISTEMA.md)
*   [Plan de Pruebas (IEEE 829)](PLAN_DE_PRUEBAS_IEEE829.md)

---
*Desarrollado como proyecto final de Ingeniería de Software - 2026*
