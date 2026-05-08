# Level Up Gamer - V0.5 (Networking)

## 📝 Descripción
Implementación de la capa de red en Android para consumir la API de forma reactiva.

## 🚀 Novedades
- **Retrofit**: Integración de Retrofit y Gson para llamadas a la API.
- **Network Interface**: Definición de `ApiService.kt`.
- **ViewModel Sync**: Sincronización del estado de la UI con datos remotos.
- **Admin Foundation**: Panel de administración básico para gestión de tipos de usuario.
- **Acceso por ID**: Capacidad del Admin para ver perfiles y carritos de otros usuarios mediante su ID.

## 🛠️ Configuración Local
- **Roles**: Usuarios registrados son asignados automáticamente como 'Usuario' (ID 3).
- **Acceso Admin**: Requiere usuario con `tipo_usuario_id = 1` en la DB.
- **IP del Servidor**: Configurar la IP local en `network/RetrofitClient.kt`.
- **Emulador**: Usar `10.0.2.2`.
- **Permisos**: Añadido `INTERNET` en el Manifest.

---
*Nota: Versión dividida en V0.5.1 (Arquitectura) y V0.5.2 (Implementación).*
