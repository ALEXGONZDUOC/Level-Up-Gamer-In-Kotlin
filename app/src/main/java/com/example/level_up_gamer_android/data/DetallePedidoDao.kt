package com.example.level_up_gamer_android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.level_up_gamer_android.model.Detalle_Pedido

@Dao
interface DetallePedidoDao {
    @Insert
    suspend fun insertar(detallePedido: Detalle_Pedido)

    @Update
    suspend fun actualizar(detallePedido: Detalle_Pedido)

    @Delete
    suspend fun eliminar(detallePedido: Detalle_Pedido)

    @Query("SELECT * FROM detalle_pedido")
    suspend fun obtenerDetallesPedido(): List<Detalle_Pedido>

    @Query("SELECT * FROM detalle_pedido WHERE Id = :id")
    suspend fun obtenerDetallePedidoPorId(id: Int): Detalle_Pedido?

    @Query("SELECT * FROM detalle_pedido WHERE Pedido_Id = :pedidoId")
    suspend fun obtenerDetallesPorPedido(pedidoId: Int): List<Detalle_Pedido>

    @Query("SELECT * FROM detalle_pedido WHERE Producto_Id = :productoId")
    suspend fun obtenerDetallesPorProducto(productoId: Int): List<Detalle_Pedido>

    @Query("""
        SELECT dp.* FROM detalle_pedido dp
        INNER JOIN pedidos p ON dp.Pedido_Id = p.Id
        WHERE p.ClienteId = :clienteId
    """)
    suspend fun obtenerDetallesPorCliente(clienteId: Int): List<Detalle_Pedido>
}
