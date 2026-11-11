package com.example.easyshop.data.nosql

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import io.objectbox.BoxStore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProdutoRepositoryTest {

    private lateinit var store: BoxStore
    private lateinit var repo: ProdutoRepository

    @Before
    fun setUp() {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        store = MyObjectBox.builder().androidContext(ctx).inMemory("test-db").build()
        repo = ProdutoRepository(store.boxFor(Produto::class.java))
    }

    @After
    fun tearDown() {
        store.close()
        store.deleteAllFiles()
    }

    @Test
    fun inserir_retornaIdMaiorQueZero() {
        assertTrue(repo.inserir(Produto(nome = "Caneta", preco = 2.0)) > 0)
    }

    @Test
    fun produtosComAtributosDiferentes_saoLidosIguais() {
        val camiseta = mutableMapOf("tamanho" to "M", "cor" to "azul")
        val notebook = mutableMapOf("ram" to "16GB", "cpu" to "i7")
        val idC = repo.inserir(Produto(nome = "Camiseta", preco = 49.9, atributos = camiseta))
        val idN = repo.inserir(Produto(nome = "Notebook", preco = 4500.0, atributos = notebook))

        val lidos = repo.listarTodos().associateBy { it.id }
        assertEquals(camiseta, lidos[idC]!!.atributos)
        assertEquals(notebook, lidos[idN]!!.atributos)
    }

    @Test
    fun mapaVazio_voltaVazio() {
        val id = repo.inserir(Produto(nome = "Caneta", preco = 2.0))
        assertTrue(repo.listarTodos().first { it.id == id }.atributos.isEmpty())
    }

    @Test
    fun listarTodos_retornaQuantidadeCorreta() {
        repeat(3) { repo.inserir(Produto(nome = "P$it", preco = 1.0 + it)) }
        assertEquals(3, repo.listarTodos().size)
    }
}
