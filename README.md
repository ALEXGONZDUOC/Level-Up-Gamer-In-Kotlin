# 🎮 Level Up Gamer - Android App

## 📝 Descripción
**Level Up Gamer** es una aplicación móvil nativa Android orientada a la venta de productos gaming (periféricos, consolas, accesorios) para el mercado chileno. Integra un backend REST con **FastAPI** y base de datos **MySQL**, con flujos completos de autenticación, catálogo, carrito, pago y gestión de direcciones.

---

## 🚀 Tecnologías Utilizadas

### Frontend (Android)
| Tecnología | Versión | Uso |
|---|---|---|
| Kotlin | 2.0 | Lenguaje principal |
| Jetpack Compose | 1.6 | UI declarativa |
| MVVM + StateFlow | — | Arquitectura y reactividad |
| Retrofit2 + Gson | 2.9 | Cliente HTTP / JSON |
| Coil (AsyncImage) | 2.5 | Carga de imágenes |
| Google Maps Compose | 4.4.1 | Mapas y geolocalización |

### Backend (API)
| Tecnología | Uso |
|---|---|
| FastAPI (Python) | Framework REST asíncrono |
| Uvicorn (ASGI) | Servidor de producción |
| PyMySQL | Conector MySQL |
| smtplib + Gmail | Envío de correos SMTP |
| python-dotenv | Variables de entorno seguras |

### Base de Datos
- **MySQL 8.0** — 5 tablas relacionales con claves foráneas y CASCADE
- Tablas: `usuario`, `producto`, `pedidos`, `detalle_pedido`, `direcciones`

---

## 🔑 Sistema de Roles

| Rol | ID | Acceso |
|---|---|---|
| Administrador | 1 | Gestión de usuarios, catálogo y dashboard |
| Supervisor | 2 | Métricas y reportes de ventas |
| Usuario Final | 3 | Catálogo, carrito, pago, pedidos y direcciones |

---

## 📱 Módulos de la Aplicación

### 🔐 Autenticación
- Login con usuario y contraseña (ver/ocultar con ícono ojito)
- Registro con validación de campos
- **Verificación de email obligatoria** — código de 6 dígitos, cuenta `activo=0` hasta verificar
- Recuperación de contraseña por código de correo (2 pasos)
- **Cambio de contraseña desde perfil** — requiere código de verificación al correo

### 🛍️ Catálogo y Carrito
- Catálogo con 30 productos en 6 categorías con imágenes `.png`
- Filtros por categoría
- Detalle de producto
- Carrito con imágenes, modificar cantidad, eliminar y total en CLP

### 💳 Pago
- Formato automático de tarjeta `XXXX XXXX XXXX XXXX`
- Formato de fecha `MM/AA`
- CVV enmascarado con `PasswordVisualTransformation`
- Validación visual con bordes rojos neón en campos inválidos
- Confirmación con número de orden en cian neón

### 📍 Direcciones
- **Pantalla "Mis Direcciones"** accesible desde Perfil
- Listar todas las direcciones como tarjetas expandibles
- Al expandir: ver detalle completo + mapa Google Maps con pin
- **Editar** dirección con switch booleano `es_principal`
- **Marcar como principal** — la anterior se desactiva automáticamente en la BD
- **Eliminar** con diálogo de confirmación
- Agregar nueva dirección con mapa interactivo en 3 pasos:
  1. Búsqueda por texto (Geocoder)
  2. Mapa con pin arrastrable
  3. Formulario auto-rellenado con coordenadas exactas

### 📦 Pedidos
- Historial de pedidos con fecha, total y dirección de envío
- Detalle de productos por pedido

### 👤 Perfil
- Editar nombre y correo
- Cambiar contraseña con verificación por código de correo
- Acceso directo a **Mis Direcciones**

---

## 🔒 Seguridad

| Medida | Implementación |
|---|---|
| Credenciales SMTP | Archivo `api/.env` en `.gitignore` |
| CVV enmascarado | `PasswordVisualTransformation` |
| Ver/ocultar contraseña | Ícono ojito en todos los formularios |
| Verificación de email | `activo=0` hasta confirmar código |
| Códigos de un solo uso | `codigos_temp` se elimina tras verificar |
| Datos de tarjeta | No se almacenan en la BD |
| Propiedad de direcciones | API valida `usuario_id` antes de modificar |

---

## 🌐 Endpoints de la API

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/usuarios` | Registro + envío código verificación |
| POST | `/usuarios/verificar` | Activar cuenta con código |
| POST | `/usuarios/recuperar` | Enviar código de recuperación |
| POST | `/usuarios/reset-password` | Cambiar contraseña con código |
| GET | `/usuarios/{id}/direcciones` | Listar direcciones del usuario |
| POST | `/direcciones` | Crear nueva dirección |
| PUT | `/direcciones/{id}` | Editar dirección (incluye `es_principal`) |
| PUT | `/direcciones/{id}/principal` | Marcar/desmarcar principal (booleano) |
| DELETE | `/direcciones/{id}` | Eliminar dirección |
| GET | `/productos` | Listar catálogo |
| POST | `/pedidos` | Crear pedido |
| GET | `/pedidos/{usuario_id}` | Historial de pedidos |

---

## 🛠️ Instalación y Configuración

### 1. Backend (API)
```bash
cd api
pip install fastapi uvicorn pymysql python-dotenv
```
Crear archivo `api/.env`:
```env
GMAIL_USER=tu_correo@gmail.com
GMAIL_PASS=tu_contraseña_de_aplicacion
```
Iniciar servidor:
```bash
uvicorn main:app --host 0.0.0.0 --port 3000 --reload
```

### 2. Base de Datos
```bash
mysql -u root -p < DB/database.sql
```

### 3. Aplicación Android
- Abrir en **Android Studio**
- Configurar IP del servidor en `RetrofitClient.kt`
  - Emulador: `http://10.0.2.2:3000/`
  - Dispositivo físico: `http://<IP_LOCAL>:3000/`
- Sincronizar Gradle y ejecutar (Min SDK 26)

---

## 📊 Estadísticas del Proyecto

| Indicador | Valor |
|---|---|
| Casos de prueba ejecutados | 43 (100% aprobados) |
| Pruebas de seguridad | 6 (100% aprobadas) |
| Mejoras implementadas | 10+ |
| Endpoints REST | 12 |
| Productos en catálogo | 30 en 6 categorías |
| Pantallas Android | 17 screens |

---

*Proyecto TPY1101 – Taller Aplicado de Programación | DuocUC | Junio 2026 | Skarlett Tropan*
