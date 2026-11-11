package com.example.easyshop.business

import com.example.easyshop.data.local.ClienteEntity
import com.example.easyshop.data.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow

class ClienteService(
    private val repository: ClienteRepository
) {
    fun listarClientes(): Flow<List<ClienteEntity>> {
        return repository.listarClientes()
    }

    suspend fun cadastrarCliente(nome: String, email: String): Long {
        require(nome.isNotBlank()) {
            "Informe o nome completo."
        }

        require(email.isNotBlank()) {
            "Informe o e-mail."
        }

        return repository.cadastrarCliente(nome, email)
    }

    suspend fun excluirCliente(id: Int) {
        repository.excluirCliente(id)
    }
}