package com.example.easyshop.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.easyshop.data.local.ClienteDao
import com.example.easyshop.data.local.ClienteEntity
import kotlinx.coroutines.flow.Flow

/** O índice único de e-mail recusou a inserção. */
class EmailDuplicadoException(causa: Throwable) : RuntimeException("E-mail duplicado", causa)

/** Contrato de acesso a dados de clientes; permite trocar por um fake nos testes do Service. */
interface ClienteDados {
    suspend fun inserir(cliente: ClienteEntity): Long
    fun listarTodos(): Flow<List<ClienteEntity>>
    suspend fun buscarPorId(id: Int): ClienteEntity?
    suspend fun buscarPorEmail(email: String): ClienteEntity?
    suspend fun excluir(id: Int)
}

/** Repository de clientes sobre o DAO do Room. Somente CRUD, sem regras de negócio. */
class ClienteRepository(
    private val clienteDao: ClienteDao
) : ClienteDados {

    override suspend fun inserir(cliente: ClienteEntity): Long =
        try {
            clienteDao.inserir(cliente)
        } catch (e: SQLiteConstraintException) {
            throw EmailDuplicadoException(e)
        }

    override fun listarTodos(): Flow<List<ClienteEntity>> = clienteDao.listarTodos()

    override suspend fun buscarPorId(id: Int): ClienteEntity? = clienteDao.buscarPorId(id)

    override suspend fun buscarPorEmail(email: String): ClienteEntity? = clienteDao.buscarPorEmail(email)

    override suspend fun excluir(id: Int) = clienteDao.excluirPorId(id)
}
