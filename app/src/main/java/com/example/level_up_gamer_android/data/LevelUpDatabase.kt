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
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
