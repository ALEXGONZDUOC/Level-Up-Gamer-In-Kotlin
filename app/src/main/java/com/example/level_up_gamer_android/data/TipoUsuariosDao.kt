package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Tipo_Usuarios

@Dao
interface TipoUsuariosDao {
    @Insert
    suspend fun insertar(tipoUsuario: Tipo_Usuarios)

    @Update
    suspend fun actualizar(tipoUsuario: Tipo_Usuarios)

    @Delete
    suspend fun eliminar(tipoUsuario: Tipo_Usuarios)

    @Query("SELECT * FROM tipo_usuario")
    suspend fun obtenerTiposUsuario(): List<Tipo_Usuarios>

    @Query("SELECT * FROM tipo_usuario WHERE id = :id")
    suspend fun obtenerTipoUsuarioPorId(id: Int): Tipo_Usuarios?

    @Query("SELECT * FROM tipo_usuario WHERE nombre = :nombre")
    suspend fun obtenerTipoUsuarioPorNombre(nombre: String): Tipo_Usuarios?

    @Query("SELECT * FROM tipo_usuario WHERE descripcion LIKE '%' || :busqueda || '%'")
    suspend fun buscarTiposUsuario(busqueda: String): List<Tipo_Usuarios>
}
