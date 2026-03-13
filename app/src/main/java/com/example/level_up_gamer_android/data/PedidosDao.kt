package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Pedidos

@Dao
interface PedidosDao {
    @Insert
    suspend fun insertar(pedido: Pedidos)

    @Update
    suspend fun actualizar(pedido: Pedidos)

    @Delete
    suspend fun eliminar(pedido: Pedidos)

    @Query("SELECT * FROM pedidos")
    suspend fun obtenerPedidos(): List<Pedidos>

    @Query("SELECT * FROM pedidos WHERE Id = :id")
    suspend fun obtenerPedidoPorId(id: Int): Pedidos?

    @Query("SELECT * FROM pedidos WHERE ClienteId = :clienteId")
    suspend fun obtenerPedidosPorCliente(clienteId: Int): List<Pedidos>

    @Query("SELECT * FROM pedidos WHERE Estado = :estado")
    suspend fun obtenerPedidosPorEstado(estado: String): List<Pedidos>

    @Query("SELECT * FROM pedidos ORDER BY FechaPedido DESC")
    suspend fun obtenerPedidosOrdenadosPorFecha(): List<Pedidos>
}
