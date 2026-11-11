package com.example.easyshop.service

import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.data.nosql.ProdutoDados
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProdutoServiceImplTest {

    private class RepositorioFake : ProdutoDados {
        val itens = mutableListOf<Produto>()
        override fun inserir(produto: Produto): Long {
            produto.id = itens.size + 1L
            itens.add(produto)
            return produto.id
        }
        override fun listarTodos(): List<Produto> = itens.toList()
    }

    private val repo = RepositorioFake()
    private val service = ProdutoServiceImpl(repo, Dispatchers.Unconfined)

    @Test
    fun cadastroValidoComAtributos_apareceNaListagem() = runTest {
        val r = service.cadastrar("Camiseta", 49.9, mapOf("tamanho" to "M", "cor" to "azul"))
        assertTrue(r.isSuccess)
        val lista = service.listar()
        assertEquals(1, lista.size)
        assertEquals(mapOf("tamanho" to "M", "cor" to "azul"), lista[0].atributos)
    }

    @Test
    fun cadastroValidoSemAtributos_sucesso() = runTest {
        assertTrue(service.cadastrar("Caneta", 2.0, emptyMap()).isSuccess)
    }

    @Test
    fun nomeVazioOuEmBranco_falha() = runTest {
        for (nome in listOf("", "   ")) {
            val r = service.cadastrar(nome, 10.0, emptyMap())
            assertTrue(r.exceptionOrNull() is IllegalArgumentException)
            assertTrue(r.exceptionOrNull()!!.message!!.isNotBlank())
        }
        assertTrue(repo.itens.isEmpty())
    }

    @Test
    fun precoZeroOuNegativo_falha() = runTest {
        for (preco in listOf(0.0, -5.0)) {
            assertTrue(service.cadastrar("Item", preco, emptyMap()).isFailure)
        }
        assertTrue(repo.itens.isEmpty())
    }

    @Test
    fun chaveVazia_eDescartada() = runTest {
        service.cadastrar("Item", 1.0, mapOf("" to "x", "  " to "y", "cor" to "preto"))
        assertEquals(mapOf("cor" to "preto"), service.listar()[0].atributos)
    }

    @Test
    fun espacosExtras_saoRemovidos() = runTest {
        service.cadastrar("  Item  ", 1.0, mapOf("  ram  " to "  16GB "))
        val p = service.listar()[0]
        assertEquals("Item", p.nome)
        assertEquals(mapOf("ram" to "16GB"), p.atributos)
    }

    @Test
    fun chavesRepetidasSemDiferenciarCaixa_mantemUltima() = runTest {
        service.cadastrar("Item", 1.0, linkedMapOf("Cor" to "azul", "cor" to "verde"))
        assertEquals(mapOf("cor" to "verde"), service.listar()[0].atributos)
    }
}
