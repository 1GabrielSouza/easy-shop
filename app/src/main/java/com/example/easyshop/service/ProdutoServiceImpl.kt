package com.example.easyshop.service

import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.data.nosql.ProdutoDados
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementação real: valida o produto, normaliza os atributos dinâmicos e
 * delega a persistência ao repository em `Dispatchers.IO`.
 */
class ProdutoServiceImpl(
    private val repository: ProdutoDados,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProdutoService {

    override suspend fun cadastrar(
        nome: String,
        preco: Double,
        atributos: Map<String, String>
    ): Result<Long> {
        val nomeLimpo = nome.trim()
        if (nomeLimpo.isEmpty()) {
            return Result.failure(ErroDeValidacao(ErroDeValidacao.Campo.NOME, "Informe o nome do produto."))
        }
        if (preco.isNaN() || preco <= 0.0) {
            return Result.failure(ErroDeValidacao(ErroDeValidacao.Campo.PRECO, "O preço deve ser maior que zero."))
        }
        val produto = Produto(nome = nomeLimpo, preco = preco, atributos = normalizar(atributos))
        return withContext(dispatcher) { runCatching { repository.inserir(produto) } }
    }

    override suspend fun listar(): List<Produto> =
        withContext(dispatcher) { repository.listarTodos() }

    /** Trim em chaves e valores, descarta chave vazia; chaves repetidas (sem diferenciar caixa) mantêm a última. */
    private fun normalizar(atributos: Map<String, String>): MutableMap<String, String> {
        val porChave = linkedMapOf<String, Pair<String, String>>()
        for ((chave, valor) in atributos) {
            val k = chave.trim()
            if (k.isEmpty()) continue
            val id = k.lowercase()
            porChave.remove(id)
            porChave[id] = k to valor.trim()
        }
        return porChave.values.toMap(linkedMapOf())
    }
}