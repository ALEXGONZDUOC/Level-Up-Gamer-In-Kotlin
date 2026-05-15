# Level Up Gamer - V0.8 (Arquitectura Modular y Seguridad Avanzada)

## 📝 Descripción
La versión **V0.8** marca el salto definitivo hacia una aplicación de nivel profesional. Se ha realizado una reestructuración profunda del código (refactorización) y se han implementado protocolos de seguridad modernos, flujos de comunicación por correo y un sistema de persistencia de sesión.

## 🚀 Evolución y Nuevas Características

### 1. Arquitectura Modular (Clean Code)
Se ha eliminado la agrupación de pantallas en archivos masivos. Ahora, **cada pantalla reside en su propio archivo `.kt`** dentro de `ui/screen/`, facilitando el mantenimiento y el diseño independiente:
- `LoginScreen`, `RegistroScreen`, `VerificationScreen`, `ForgotPasswordScreen`.
- `HomeScreen` (Tienda), `SearchScreen`, `CartScreen`.
- `AddAddressScreen`, `AddressSelectionScreen`, `PaymentScreen`, `OrderConfirmationScreen`.
- `AdminDashboardScreen`, `AdminUserManagementScreen`, `SupervisorScreen`.

### 2. Seguridad y Mensajería (SMTP & Códigos)
Se introdujo un sistema de autenticación de dos pasos basado en **códigos numéricos de 6 dígitos**:
- **Verificación de Cuenta**: Los nuevos usuarios deben validar su correo mediante un código único enviado por la API para activar su cuenta.
- **Recuperación de Contraseña**: Flujo completo de "Olvidé mi contraseña" que permite restablecer el acceso de forma segura.
- **Correos HTML Profesionales**: La API envía notificaciones dinámicas para Bienvenida, Seguridad y Confirmación de Pedidos.

### 3. Experiencia de Usuario (UX) y Navegación Dinámica
- **Modo Invitado**: La aplicación inicia directamente en la Tienda. Los usuarios pueden explorar el catálogo sin necesidad de loguearse previamente.
- **Persistencia de Sesión (Caché)**: Implementación de `SessionManager`. Una vez iniciada la sesión, los datos se guardan localmente para que el usuario no tenga que reingresar sus credenciales al reabrir la app.
- **Barra Inferior Inteligente**: 
    - Identifica el rol del usuario (Admin, Supervisor o Cliente) y muestra solo las funciones pertinentes.
    - **Botón Multifunción**: El botón de acción derecho cambia dinámicamente:
        - `Salir` (Rojo): Si estás en tu pantalla de inicio (hace logout).
        - `Volver` (Azul): Si estás navegando en pantallas secundarias.
        - `Entrar`: Si eres un invitado en la tienda.

### 4. Lógica de Negocio y Logística
- **Gestión Autónoma de Direcciones**: Los usuarios pueden añadir nuevos domicilios directamente desde la interfaz, integrándose automáticamente con Google Maps.
- **Pedidos Detallados**: Soporte transaccional para compras multi-producto con registro en la tabla `detalle_pedido`.
- **Buscador Avanzado**: Filtrado de productos por texto y por **ID de Categoría**.

## 🛠️ Requisitos Técnicos
1. **Base de Datos**: Ejecutar `V0.8/DB/database.sql` para actualizar la tabla `usuario` (campos `verificado` y `codigo_auth`).
2. **API (FastAPI)**: Configurar el archivo `.env` con credenciales `GMAIL_USER` y `GMAIL_PASS` para habilitar el sistema de correos.

---
*V0.8: Una base de código sólida, modular y segura, lista para el despliegue final.*
