# Level Up Gamer - V0.6 (Sistema Transaccional y Roles)

## 📝 Descripción
La versión **V0.6** representa el hito más importante en el desarrollo del sistema, transformando la aplicación de un prototipo visual a una **plataforma e-commerce transaccional real**. Se introducen la lógica de negocio compleja, la integridad de inventario y una jerarquía de acceso basada en roles.

## 🚀 Evolución desde V0.5.2
A diferencia de la versión anterior (que era un CRUD básico), la V0.6 implementa:
- **Persistencia Real**: Las compras ya no son simuladas; afectan el stock y las estadísticas en la base de datos.
- **Jerarquía de Roles**: Implementación de lógica diferenciada para Admin, Supervisor y Usuario.
- **Inteligencia de Negocio**: Capacidad de analizar el éxito de los productos mediante datos agregados.

## 🛠️ Características Técnicas

### 1. Backend & API (FastAPI)
- **Endpoint `/productos/comprar`**: Realiza el descuento automático de `cantidad` y el incremento de `total_vendido` de forma atómica.
- **Endpoints de Estadísticas**: 
    - `GET /estadisticas/ventas-totales`: Consultas filtradas por periodo (día, semana, mes).
    - `GET /estadisticas/top-productos`: Clasificación de productos por volumen de venta.
- **Control de Acceso**: Los usuarios incluyen el campo `activo` (booleano) para permitir o denegar el login desde el panel administrativo.

### 2. Lógica de Negocio (ViewModel)
- **Gestión de Compra**: El `FormularioViewModel` ahora orquestra el flujo completo: validación de carrito -> envío a API -> limpieza de estado local -> refresco automático del catálogo.
- **Sincronización de Estadísticas**: Carga asíncrona de datos para los paneles de reporte.

### 3. Interfaz de Usuario (Compose)
- **Panel de Supervisor (NUEVO)**: 
    - Visualización de ventas mediante `StatCards`.
    - Lista de "Inventario Crítico" para productos con stock bajo.
- **Admin Dashboard Evolucionado**:
    - Opción de alternar entre "Gestionar Sistema" y "Usar como Cliente".
    - Gestión de usuarios con Toggles para activar/desactivar cuentas en tiempo real.
- **Flujo de Usuario**: Experiencia de compra fluida desde el catálogo hasta el carrito funcional.

## 📊 Base de Datos
Es necesario ejecutar el script `V0.6/DB/database.sql` para habilitar:
- Tabla `pedidos` para el registro histórico.
- Campos de tracking comercial en la tabla `producto`.

---
*V0.6: El sistema ya no solo muestra productos, ahora gestiona un negocio.*
