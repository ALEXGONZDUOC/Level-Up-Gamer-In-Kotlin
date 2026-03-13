
# Guion para Presentación de la Base de Datos: "Level Up Gamer"

---

### **Diapositiva 1: Título**

**(Título en pantalla: Arquitectura de Datos: El Corazón de Level Up Gamer)**

**Presentador:**
"Hola a todos. Hoy vamos a sumergirnos en el núcleo de nuestra aplicación, Level Up Gamer: su arquitectura de datos. Veremos cómo hemos estructurado nuestra base de datos para que sea robusta, escalable y eficiente, permitiendo que toda la experiencia del usuario funcione de manera fluida."

---

### **Diapositiva 2: La Tecnología - ¿Por qué Room?**

**(Iconos en pantalla: Kotlin, Android, Room, SQLite)**

**Presentador:**
"Para nuestra base de datos local en Android, elegimos **Room**. Room no es una base de datos en sí misma, sino una capa de abstracción inteligente sobre SQLite, que es la base de datos estándar en Android.

¿Por qué Room?
1.  **Seguridad en tiempo de compilación:** Room verifica nuestras consultas SQL mientras escribimos el código, no cuando la aplicación se está ejecutando. Esto nos ahorra incontables horas de depuración al evitar errores comunes de SQL.
2.  **Menos código repetitivo:** Simplifica enormemente las operaciones de la base de datos. En lugar de manejar cursores y constructores de consultas manualmente, trabajamos con objetos de Kotlin simples y claros.
3.  **Integración con Corrutinas:** Se integra perfectamente con las corrutinas de Kotlin, lo que nos permite realizar consultas a la base de datos en un hilo secundario de forma muy sencilla, manteniendo la interfaz de usuario siempre fluida y receptiva."

---

### **Diapositiva 3: Vista General del Esquema**

**(Diagrama de Entidad-Relación simplificado en pantalla mostrando las tablas y sus conexiones)**

**Presentador:**
"Este es el mapa de nuestra base de datos. A primera vista, puede parecer complejo, pero está organizado en torno a tres conceptos clave: **Usuarios**, **Productos** y **Transacciones**.

-   El módulo de **Usuarios** gestiona quién está usando la aplicación.
-   El módulo de **Productos** define qué vendemos.
-   Y el módulo de **Transacciones** conecta a los usuarios con los productos a través de pedidos y reseñas.

Vamos a desglosar cada parte."

---

### **Diapositiva 4: Módulo de Usuarios**

**(Diagrama mostrando las tablas `usuario`, `tipo_usuario` y `clientes` conectadas)**

**Presentador:**
"Todo comienza con el usuario.

1.  **Tabla `usuario`:** Es nuestra tabla principal de autenticación. Aquí guardamos la información esencial para el inicio de sesión: un ID único, nombre de usuario, correo y contraseña cifrada. También incluye un estado `activo` y la fecha de creación.

2.  **Tabla `tipo_usuario`:** Esta tabla define los roles, como 'Administrador' o 'Cliente'. Al separar los roles, podemos asignar permisos y funcionalidades específicas a cada tipo de usuario de manera flexible. Un usuario tiene un solo tipo, lo que define lo que puede ver y hacer en la app.

3.  **Tabla `clientes`:** Mientras que `usuario` es para la autenticación, la tabla `clientes` almacena la información del *perfil* del comprador, como su nombre completo, teléfono y dirección de envío. Está vinculada directamente a un `usuario`. Esta separación nos permite tener usuarios que no son necesariamente clientes (como los administradores)."

---

### **Diapositiva 5: Módulo de Catálogo**

**(Diagrama enfocado en la tabla `producto` y `reseñas`)**

**Presentador:**
"Ahora, veamos qué vendemos.

1.  **Tabla `producto`:** Es el corazón de nuestro catálogo. Cada fila es un artículo en venta. Contiene toda la información que ves en la pantalla: nombre, descripción, precio, categoría y, muy importante, la cantidad en stock para gestionar el inventario. También almacenamos rutas a las imágenes del producto.

2.  **Tabla `reseñas`:** La confianza es clave en el e-commerce. La tabla `reseñas` permite a los clientes calificar y comentar sobre los productos que han comprado. Está vinculada tanto a `clientes` como a `producto`, asegurando que solo los clientes reales puedan dejar reseñas sobre productos específicos. Esto enriquece nuestro catálogo con contenido generado por el usuario."

---

### **Diapositiva 6: Módulo de Transacciones**

**(Diagrama mostrando `pedidos` y `detalle_pedido` conectando a `clientes` y `producto`)**

**Presentador:**
"Aquí es donde ocurre la magia: la compra.

1.  **Tabla `pedidos`:** Cuando un cliente finaliza su compra, se crea un registro en esta tabla. Funciona como el encabezado de una factura: almacena quién hizo el pedido (`ClienteId`), cuándo se hizo, el estado actual ('Procesando', 'Enviado', etc.) y el monto total.

2.  **Tabla `detalle_pedido`:** Un pedido puede contener múltiples productos. Esta tabla de detalle desglosa cada pedido. Cada fila representa un producto dentro de un pedido, especificando qué producto se compró (`Producto_Id`), cuántas unidades (`Cantidad`) y el subtotal para esa línea. Esta es una tabla intermedia clásica que resuelve la relación de 'muchos a muchos' entre pedidos y productos."

---

### **Diapositiva 7: Conclusión**

**(Frase en pantalla: "Una base sólida para una gran experiencia")**

**Presentador:**
"Como hemos visto, nuestra base de datos no es solo un lugar para almacenar datos. Es un sistema bien estructurado que refleja la lógica de nuestro negocio y potencia cada característica de la aplicación.

Gracias al diseño relacional y al poder de Room, hemos creado una base de datos que es:
-   **Organizada:** Cada pieza de información tiene su lugar.
-   **Relacionada:** Los datos están conectados de manera lógica, permitiendo consultas complejas pero eficientes.
-   **Escalable:** Esta estructura nos permitirá añadir nuevas funcionalidades en el futuro, como historiales de compra avanzados o sistemas de recomendación, de una manera ordenada.

Gracias por su atención. ¿Tienen alguna pregunta?"
