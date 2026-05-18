# 🎮 Level Up Gamer - Manual de Funcionamiento y Estructura

Este documento detalla el funcionamiento técnico, las características y la arquitectura del sistema **Level Up Gamer**, que consta de una aplicación Android (Kotlin/Compose) y un Backend (FastAPI/MySQL).

---

## 🏗️ Arquitectura del Sistema
- **Frontend:** Android Nativo con Jetpack Compose. Utiliza el patrón **MVVM** (Model-View-ViewModel) con un estado centralizado en `FormularioViewModel`.
- **Backend:** FastAPI (Python) con conexión directa a MySQL mediante `pymysql`.
- **Comunicación:** Retrofit para peticiones RESTful.

---

## 🔑 Roles de Usuario
El sistema se comporta de manera distinta según el `tipo_usuario_id` del usuario logueado:

1. **Administrador (ID: 1):**
   - Acceso total a la gestión de usuarios.
   - Capacidad para activar o desactivar cuentas.
   - Edición y eliminación de productos del catálogo.
   - Navegación extendida al panel de control.

2. **Usuario Normal (ID: 2):**
   - Navegación por el catálogo de productos.
   - Gestión de carrito de compras.
   - Selección de direcciones y finalización de pedidos.
   - Gestión de perfil personal.

3. **Supervisor (ID: 3):**
   - Acceso a herramientas de análisis y estadísticas.
   - Visualización de ventas por periodo (Día, Semana, Mes).
   - Detalle de ventas por producto individual.
   - Gestión de stock (Creación/Edición de productos).

---

## 📱 Pantallas Principales (Screens)

### 🔓 Autenticación
- **SplashScreen:** Pantalla de carga con logo animado.
- **LoginScreen:** Validación de credenciales. Verifica si la cuenta está activa.
- **RegistroScreen:** Creación de nuevas cuentas de usuario (Rol 'Usuario' por defecto).

### 🛒 Flujo de Compra
- **HomeScreen:** Catálogo interactivo con animaciones de entrada. Permite añadir al carrito y filtrar acciones por rol.
- **CartScreen:** Resumen de compra, edición de cantidades y cálculo de totales.
- **AddressSelectionScreen:** Gestión de direcciones de entrega (Soporta múltiples direcciones y marcar una como principal).
- **PaymentScreen:** Simulación de pasarela de pago y envío del pedido al servidor.
- **OrderConfirmationScreen:** Recibo visual del pedido con número de orden generado (Ej: LVL-101).

### ⚙️ Gestión y Perfil
- **UpdateProfileScreen:** Edición de datos personales (Nombre, Email, Contraseña).
- **ProductEditorScreen:** Formulario para crear o editar productos (Solo para Admin/Supervisor).

### 📊 Paneles de Control (Admin/Supervisor)
- **AdminDashboardScreen:** Puerta de entrada para administradores.
- **AdminUserManagementScreen:** Listado y buscador de usuarios con capacidad de edición de roles y estado de cuenta.
- **TotalVentasScreen:** Dashboard estadístico con ranking de productos más vendidos y sumatoria de ingresos.
- **ProductSalesDetailScreen:** Gráfica detallada de cuántas unidades se han vendido de un producto específico cada día.

---

## 🛠️ Funciones Críticas del Backend (API)

- **`POST /pedidos` (Transacción Segura):** 
    - Calcula el total en el servidor.
    - Verifica stock disponible antes de procesar.
    - Descuenta automáticamente el stock de los productos.
    - Inserta cabecera y detalles en una sola transacción atómica.
- **`GET /estadisticas/top-productos`:** Calcula dinámicamente el ranking basado en la tabla `detalle_pedido`.
- **`PUT /usuarios/{id}`:** Endpoint unificado para que administradores o el propio usuario actualicen sus datos de forma segura.

---

## 🎨 Componentes Reutilizables (UI Kit)
- **AppBottomBar:** Navegación inteligente que cambia sus botones dinámicamente según el rol del usuario.
- **ProductoCard:** Tarjeta de producto que muestra stock, precio y opciones de gestión (Editar/Eliminar/Ver Ventas) según permisos.
- **GradientSurface:** Fondo estético unificado para toda la aplicación.
- **CustomTextField / CustomButton:** Estilización consistente basada en el tema del proyecto.

---
*Documentación generada el 08 de Abril de 2026*
