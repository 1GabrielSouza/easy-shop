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

    /** Mais novo primeiro: o cliente recém-cadastrado aparece no topo da lista. */
    @Query("SELECT * FROM clientes ORDER BY id DESC")
    fun listarTodos(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): ClienteEntity?

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}
