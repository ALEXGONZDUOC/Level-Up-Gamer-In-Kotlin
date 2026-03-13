package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Reseñas

@Dao
interface ResenasDao {
    @Insert
    suspend fun insertar(resena: Reseñas)

    @Update
    suspend fun actualizar(resena: Reseñas)

    @Delete
    suspend fun eliminar(resena: Reseñas)

    @Query("SELECT * FROM reseñas")
    suspend fun obtenerResenas(): List<Reseñas>

    @Query("SELECT * FROM reseñas WHERE id = :id")
    suspend fun obtenerResenaPorId(id: Int): Reseñas?

    @Query("SELECT * FROM reseñas WHERE cliente_id = :clienteId")
    suspend fun obtenerResenasPorCliente(clienteId: Int): List<Reseñas>

    @Query("SELECT * FROM reseñas WHERE producto_id = :productoId")
    suspend fun obtenerResenasPorProducto(productoId: Int): List<Reseñas>

    @Query("SELECT AVG(calificacion) FROM reseñas WHERE producto_id = :productoId")
    suspend fun obtenerCalificacionPromedio(productoId: Int): Float?

    @Query("SELECT * FROM reseñas WHERE calificacion >= :minCalificacion")
    suspend fun obtenerResenasPorCalificacion(minCalificacion: Int): List<Reseñas>

    @Query("SELECT * FROM reseñas ORDER BY fecha DESC")
    suspend fun obtenerResenasOrdenadasPorFecha(): List<Reseñas>
}
