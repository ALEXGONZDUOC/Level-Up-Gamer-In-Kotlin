package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Clientes

@Dao
interface ClientesDao {
    @Insert
    suspend fun insertar(cliente: Clientes)

    @Update
    suspend fun actualizar(cliente: Clientes)

    @Delete
    suspend fun eliminar(cliente: Clientes)

    @Query("SELECT * FROM clientes")
    suspend fun obtenerClientes(): List<Clientes>

    @Query("SELECT * FROM clientes WHERE Id = :id")
    suspend fun obtenerClientePorId(id: Int): Clientes?

    @Query("SELECT * FROM clientes WHERE usuario_id = :usuarioId")
    suspend fun obtenerClientePorUsuarioId(usuarioId: Int): Clientes?

    @Query("SELECT * FROM clientes WHERE nombre_completo LIKE '%' || :busqueda || '%'")
    suspend fun buscarClientes(busqueda: String): List<Clientes>

    @Query("SELECT * FROM clientes WHERE telefono = :telefono")
    suspend fun obtenerClientePorTelefono(telefono: String): Clientes?
}
