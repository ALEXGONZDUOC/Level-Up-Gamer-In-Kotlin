package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Producto

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertar(producto: Producto)

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query("SELECT * FROM Producto")
    suspend fun obtenerProductos(): List<Producto>

    @Query("SELECT * FROM Producto WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): Producto?

    @Query("SELECT * FROM Producto WHERE categoria = :categoria")
    suspend fun obtenerProductosPorCategoria(categoria: String): List<Producto>

    @Query("SELECT * FROM Producto WHERE nombre LIKE '%' || :busqueda || '%'")
    suspend fun buscarProductos(busqueda: String): List<Producto>
}
