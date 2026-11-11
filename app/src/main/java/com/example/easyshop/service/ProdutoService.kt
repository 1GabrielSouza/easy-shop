package com.example.easyshop.service

import com.example.easyshop.data.nosql.Produto

/** Regras de negócio do catálogo de produtos. */
interface ProdutoService {
    suspend fun cadastrar(nome: String, preco: Double, atributos: Map<String, String>): Result<Long>
    suspend fun listar(): List<Produto>
}
