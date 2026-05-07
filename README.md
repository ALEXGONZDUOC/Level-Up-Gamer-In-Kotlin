# Proyecto: Level Up Gamer Android - V0.5.1 (Arquitectura de Red)

## 1. Descripción General

**Level Up Gamer Android V0.5.1** marca un hito fundamental en el proyecto: la transición de una arquitectura local a una arquitectura cliente-servidor basada en la nube. Esta versión introduce la capa de red utilizando **Retrofit**, permitiendo que la aplicación consuma datos reales desde una API externa en lugar de depender de una base de datos local o datos estáticos.

### Características Principales

*   **Integración con API:** Primera implementación de la comunicación con el servidor backend (FastAPI).
*   **Autenticación Base:** Inicio de sesión validado directamente contra el servidor remoto.
*   **Catálogo Dinámico:** Los productos se cargan en tiempo real desde la base de datos centralizada del servidor.
*   **Navegación Intuitiva:** Flujo de navegación optimizado entre login y catálogo.

### Stack Tecnológico

*   **Lenguaje de Programación:** 100% [Kotlin](https://kotlinlang.org/).
*   **Interfaz de Usuario (UI):** [Jetpack Compose](https://developer.android.com/jetpack/compose).
*   **Arquitectura de Software:** **MVVM (Model-View-ViewModel)**.
*   **Capa de Red:** [Retrofit 2](https://square.github.io/retrofit/), la librería estándar de la industria para peticiones HTTP en Android.
*   **Serialización:** [Gson](https://github.com/google/gson) para la conversión automática de JSON a objetos Kotlin.
*   **Manejo de Asincronía:** **Corrutinas de Kotlin** y `StateFlow` para manejar peticiones de red sin bloquear la interfaz de usuario.

## 2. Arquitectura del Proyecto (MVVM + Networking)

El proyecto ha sido simplificado para enfocarse en la eficiencia de la red, eliminando la dependencia de base de datos local (Room) para centralizar la verdad en el servidor.

*   `app/src/main/java/com/example/level_up_gamer_android`
    *   `model/`: Clases de datos (`data class`) que representan las entidades del negocio (Usuario, Producto).
    *   `network/`: 
        *   `ApiService.kt`: Interfaz que define los endpoints de la API (GET /productos, POST /login, etc.).
        *   `RetrofitClient.kt`: Configuración centralizada de Retrofit, incluyendo la URL base del servidor.
    *   `viewmodel/`: 
        *   `FormularioViewModel.kt`: Gestiona el estado de la aplicación, realizando llamadas asíncronas a la API y exponiendo los resultados a la UI mediante flujos reactivos.
    *   `ui/`: Pantallas y componentes construidos íntegramente con Jetpack Compose.

## 3. Transición a API

A diferencia de versiones anteriores, V0.5.1 ya no utiliza DAOs ni bases de datos SQLite locales. Todo el flujo de datos es asíncrono:

1.  **Petición:** El ViewModel invoca un método de `ApiService`.
2.  **Suspensión:** La corrutina suspende la ejecución mientras espera la respuesta del servidor (evitando que la app se "congele").
3.  **Respuesta:** Retrofit convierte el JSON recibido en objetos del `model`.
4.  **Actualización:** El `StateFlow` emite el nuevo estado y la UI se recompone automáticamente.

## 4. Cómo Ejecutar

1.  **Servidor API:** Asegúrese de tener el servidor FastAPI (ubicado en la carpeta `api/`) ejecutándose en su PC.
2.  **Configuración de IP:** En `RetrofitClient.kt`, verifique que la URL apunte a `10.0.2.2` si está usando el emulador de Android Studio.
3.  **Compilar:** Use Android Studio para sincronizar Gradle y ejecutar la aplicación.

---
*Nota: Esta versión es el primer paso hacia el sistema completo sincronizado que se alcanza en la V0.9.*
