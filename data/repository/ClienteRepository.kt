package com.example.easyshop.data.repository

import com.example.easyshop.data.local.ClienteDao
import com.example.easyshop.data.local.ClienteEntity
import kotlinx.coroutines.flow.Flow

class ClienteRepository(
    private val clienteDao: ClienteDao
) {
    fun listarClientes(): Flow<List<ClienteEntity>> =
        clienteDao.listarTodos()

    suspend fun cadastrarCliente(nomeCompleto: String, email: String): Long {
        require(nomeCompleto.isNotBlank()) {
            "O nome completo é obrigatório."
        }

        require(email.isNotBlank()) {
            "O e-mail é obrigatório."
        }

        return clienteDao.inserir(
            ClienteEntity(
                nomeCompleto = nomeCompleto.trim(),
                email = email.trim().lowercase()
            )
        )
    }

    suspend fun buscarClientePorId(id: Int): ClienteEntity? =
        clienteDao.buscarPorId(id)

    suspend fun excluirCliente(id: Int) =
        clienteDao.excluirPorId(id)
}