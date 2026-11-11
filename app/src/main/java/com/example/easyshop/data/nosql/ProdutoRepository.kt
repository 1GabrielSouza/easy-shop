package com.example.easyshop.data.nosql

import io.objectbox.Box

/** Contrato de acesso a dados de produtos; permite trocar por um fake nos testes do Service. */
interface ProdutoDados {
    fun inserir(produto: Produto): Long
    fun listarTodos(): List<Produto>
}

/** Repository de produtos sobre um [Box] do ObjectBox. Somente CRUD, sem regras de negócio. */
class ProdutoRepository(private val box: Box<Produto>) : ProdutoDados {
    override fun inserir(produto: Produto): Long = box.put(produto)

    override fun listarTodos(): List<Produto> = box.all
}
