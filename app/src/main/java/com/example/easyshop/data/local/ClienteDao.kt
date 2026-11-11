package com.example.easyshop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(cliente: ClienteEntity): Long

    @Query("SELECT * FROM clientes ORDER BY nomeCompleto ASC")
    fun listarTodos(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): ClienteEntity?

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}