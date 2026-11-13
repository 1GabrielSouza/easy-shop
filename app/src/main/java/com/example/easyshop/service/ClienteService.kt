package com.example.easyshop.service

import com.example.easyshop.data.local.ClienteEntity
import kotlinx.coroutines.flow.Flow

/** Regras de negócio do cadastro de clientes (banco relacional, Room). */
interface ClienteService {
    /**
     * Valida e grava um cliente.
     * Sucesso: o id gerado. Falha: [ErroDeValidacao] com o campo e a mensagem para a tela.
     */
    suspend fun cadastrar(nome: String, email: String): Result<Long>

    /** Lista reativa: emite de novo sempre que a tabela muda. Mais novo primeiro. */
    fun listar(): Flow<List<ClienteEntity>>

    suspend fun excluir(id: Int)
}
