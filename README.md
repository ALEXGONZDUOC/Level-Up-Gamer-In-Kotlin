# Proyecto: Level Up Gamer Android

## 1. Descripción General

**Level Up Gamer Android** es una aplicación de comercio electrónico robusta y moderna, desarrollada nativamente para la plataforma Android. Su propósito es ofrecer una experiencia de compra fluida y atractiva para entusiastas de los videojuegos y la cultura gamer. Los usuarios pueden registrarse, iniciar sesión, explorar un catálogo detallado de productos, gestionar un carrito de compras y mucho más.

La aplicación ha sido construida siguiendo las mejores prácticas y las tecnologías más actuales recomendadas por Google, garantizando un rendimiento óptimo, una base de código mantenible y una experiencia de usuario de alta calidad.

### Características Principales

*   **Autenticación de Usuarios:** Sistema completo de registro e inicio de sesión.
*   **Catálogo de Productos:** Visualización de productos con imágenes, descripciones, precios y categorías.
*   **Carrito de Compras:** Funcionalidad para añadir, modificar y eliminar productos del carrito, con cálculo de subtotales y total en tiempo real.
*   **Navegación Intuitiva:** Flujo de navegación claro y coherente entre las diferentes pantallas de la aplicación.
*   **Gestión de Perfil (Futuro):** La arquitectura está preparada para futuras implementaciones como la visualización de historial de pedidos y la gestión de datos de perfil.

### Stack Tecnológico

*   **Lenguaje de Programación:** 100% [Kotlin](https://kotlinlang.org/), aprovechando sus características de seguridad, concisión y las corrutinas para la asincronía.
*   **Interfaz de Usuario (UI):** [Jetpack Compose](https://developer.android.com/jetpack/compose), el moderno toolkit declarativo de Google para construir interfaces nativas. Esto permite un desarrollo de UI más rápido y un código más limpio y reutilizable.
*   **Arquitectura de Software:** **MVVM (Model-View-ViewModel)**, un patrón de diseño que separa la lógica de la interfaz de usuario de la lógica de negocio. Esto resulta en un código más organizado, testeable y fácil de mantener.
*   **Base de Datos Local:** [Room Persistence Library](https://developer.android.com/training/data-storage/room), una capa de abstracción sobre SQLite que facilita un acceso robusto y eficiente a la base de datos local del dispositivo.
*   **Gestión de Navegación:** [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation), que permite gestionar de forma centralizada y visual el flujo de pantallas (Composables) en la aplicación.
*   **Manejo de Asincronía:** Se utilizan **Corrutinas de Kotlin** y `StateFlow` para manejar operaciones en segundo plano (como consultas a la base de datos) de manera eficiente y para actualizar la UI de forma reactiva y sin esfuerzo.

## 2. Arquitectura del Proyecto (MVVM)

El proyecto está estructurado siguiendo los principios de la arquitectura MVVM, lo que promueve una clara separación de responsabilidades entre los diferentes componentes del software.

*   `app/src/main/java/com/example/level_up_gamer_android`
    *   `model/`: Contiene las clases de datos (`data class`) que definen las entidades de la aplicación. Estas clases, anotadas con `@Entity` de Room, representan las tablas en la base de datos SQLite.
        *   **Ejemplos:** `Usuario.kt`, `Producto.kt`, `Pedidos.kt`.
    *   `data/`: Se encarga de la lógica de acceso a los datos.
        *   **DAOs (Data Access Objects):** Interfaces como `ProductoDao.kt` donde se definen, mediante anotaciones de Room, las consultas a la base de datos (SELECT, INSERT, UPDATE, DELETE).
        *   `LevelUpDatabase.kt`: Es el componente central de Room. Configura la base de datos, lista todas las entidades (tablas) y proporciona acceso a los DAOs. Utiliza un patrón Singleton para garantizar una única instancia de la base de datos, optimizando el rendimiento.
    *   `viewmodel/`: Contiene las clases `ViewModel`.
        *   `FormularioViewModel.kt`: Actúa como el intermediario entre la capa de datos y la UI. Contiene la lógica de negocio (validaciones, cálculos, etc.) y gestiona el estado de la UI, exponiéndolo de forma reactiva mediante `StateFlow`.
    *   `ui/`: Contiene todo el código relacionado con la interfaz de usuario, construido con Jetpack Compose.
        *   `screen/`: Composables de alto nivel que representan pantallas completas (ej. `HomeScreen.kt`, `LoginScreen.kt`).
        *   `components/`: Pequeños componentes de UI reutilizables (Botones, Tarjetas, Campos de Texto) que se usan para construir las pantallas.
        *   `theme/`: Define la guía de estilo visual de la aplicación: `Color.kt` (paleta de colores), `Type.kt` (tipografías como "Orbitron") y `Theme.kt` (el tema global de la app).
    *   `navigation/`: Gestiona el flujo de navegación.
        *   `AppNavigation.kt`: Define el "grafo de navegación", especificando todas las rutas (pantallas) y las transiciones entre ellas.

## 3. Base de Datos (Room)

La persistencia de datos se maneja íntegramente con Room. Para un detalle completo del esquema, consulte el archivo `database_schema.txt`.

### Flujo de Datos Reactivo

Un aspecto clave de la arquitectura es cómo los datos fluyen desde la base de datos hasta la interfaz de usuario de manera reactiva.

1.  **Consulta en el ViewModel:** El `FormularioViewModel` solicita datos a través de un DAO (por ejemplo, `productoDao().obtenerProductos()`).
2.  **Exposición con StateFlow:** El ViewModel expone los datos obtenidos a través de un `StateFlow`. `StateFlow` es un flujo de datos que mantiene el último estado y lo emite a cualquier observador.
3.  **Observación en la UI:** En una pantalla de Compose (como `HomeScreen.kt`), se utiliza `collectAsState()` para suscribirse al `StateFlow` del ViewModel.
4.  **Recomposición Automática:** Cada vez que los datos en el `StateFlow` cambian (por ejemplo, se añade un nuevo producto), Jetpack Compose detecta el cambio y "recompone" automáticamente solo las partes de la UI que dependen de esos datos, asegurando que la pantalla siempre refleje el estado actual de la aplicación de forma eficiente.

## 4. Cómo Compilar y Ejecutar

1.  **Clonar el Repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```
2.  **Abrir en Android Studio:**
    *   Abre Android Studio (se recomienda la versión más reciente).
    *   Selecciona "Open an existing project".
    *   Navega hasta la carpeta del proyecto clonado y selecciónala.
3.  **Sincronizar Gradle:** Android Studio detectará automáticamente los archivos `build.gradle.kts` y sincronizará el proyecto, descargando todas las dependencias necesarias.
4.  **Ejecutar la Aplicación:**
    *   Selecciona un dispositivo virtual (Emulador) o conecta un dispositivo físico.
    *   Presiona el botón "Run 'app'" (o usa el atajo `Shift + F10`).

## 5. Limpieza del Proyecto para Exportación

Para crear una versión limpia del proyecto, ideal para compartir o archivar, se pueden seguir estos pasos desde la terminal integrada de Android Studio:

*   **Para Windows:**
    ```bash
    gradlew clean
    ```
*   **Para macOS/Linux:**
    ```bash
    ./gradlew clean
    ```

Este comando eliminará los directorios `build` de todo el proyecto, que contienen archivos generados y compilados. Los directorios `.idea` (configuración de Android Studio) y `.gradle` (caché de Gradle) no se eliminan, ya que son necesarios para que el IDE funcione correctamente al importar el proyecto.