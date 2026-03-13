package com.example.level_up_gamer_android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.level_up_gamer_android.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        Pedidos::class,
        Detalle_Pedido::class,
        Clientes::class,
        Reseñas::class,
        Tipo_Usuarios::class
    ],
    version = 1
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun pedidosDao(): PedidosDao
    abstract fun detallePedidoDao(): DetallePedidoDao
    abstract fun clientesDao(): ClientesDao
    abstract fun resenasDao(): ResenasDao
    abstract fun tipoUsuariosDao(): TipoUsuariosDao

    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelupgamer.db"
                )
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                
                                val usuariosExistentes = database.usuarioDao().obtenerUsuarios()
                                if (usuariosExistentes.isEmpty()) {
                                    insertMockData(database)
                                }
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun insertMockData(database: LevelUpDatabase) {
            val usuarioDao = database.usuarioDao()
            val productoDao = database.productoDao()
            val tipoUsuariosDao = database.tipoUsuariosDao()

            MOCK_TIPO_USUARIOS.forEach { tipoUsuariosDao.insertar(it) }
            MOCK_USUARIOS.forEach { usuarioDao.insertar(it) }
            MOCK_PRODUCTOS.forEach { productoDao.insertar(it) }
        }

        
        val MOCK_USUARIOS = listOf(
            Usuario(id = 1, nombre = "admin", contrasena = "1234", email = "admin@gamer.com", tipo_usuario_id = 1, activo = true, fecha_creacion = "2024-01-01"),
            Usuario(id = 2, nombre = "user", contrasena = "1234", email = "user@gamer.com", tipo_usuario_id = 2, activo = true, fecha_creacion = "2024-01-02")
        )

        val MOCK_PRODUCTOS = listOf(
            Producto(id = 1, codigo = 1001.0, nombre = "PlayStation 5", categoria = "Consolas", descripcion = "Consola de última generación", precio = 499.99, cantidad = 10, imagenUrl = "https://example.com/ps5.png"),
            Producto(id = 2, codigo = 1002.0, nombre = "Xbox Series X", categoria = "Consolas", descripcion = "Consola Microsoft de alta potencia", precio = 499.99, cantidad = 8, imagenUrl = "https://example.com/xbox_series_x.png"),
            Producto(id = 3, codigo = 2001.0, nombre = "Nintendo Switch OLED", categoria = "Consolas", descripcion = "Consola híbrida con pantalla OLED", precio = 349.99, cantidad = 15, imagenUrl = "https://example.com/switch_oled.png"),
            Producto(id = 4, codigo = 3001.0, nombre = "FIFA 24", categoria = "Juegos", descripcion = "Juego de fútbol para múltiples plataformas", precio = 59.99, cantidad = 20, imagenUrl = "https://example.com/fifa24.png"),
            Producto(id = 5, codigo = 3002.0, nombre = "Call of Duty Modern Warfare III", categoria = "Juegos", descripcion = "Juego de acción en primera persona", precio = 69.99, cantidad = 12, imagenUrl = "https://example.com/cod_mw3.png"),
            Producto(id = 6, codigo = 4001.0, nombre = "Control DualSense PS5", categoria = "Accesorios", descripcion = "Control inalámbrico para PlayStation 5", precio = 69.99, cantidad = 25, imagenUrl = "https://example.com/dualsense.png")
        )

        val MOCK_TIPO_USUARIOS = listOf(
            Tipo_Usuarios(id = 1, nombre = "Administrador", descripcion = "Usuario con permisos completos"),
            Tipo_Usuarios(id = 2, nombre = "Cliente", descripcion = "Usuario cliente estándar")
        )
    }
}
