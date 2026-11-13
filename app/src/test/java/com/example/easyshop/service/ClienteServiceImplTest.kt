package com.example.easyshop.service

import com.example.easyshop.data.local.ClienteEntity
import com.example.easyshop.data.repository.ClienteDados
import com.example.easyshop.data.repository.EmailDuplicadoException
import com.example.easyshop.service.ErroDeValidacao.Campo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClienteServiceImplTest {

    /** Imita o Room: id autoincremento, e-mail único e lista do mais novo para o mais antigo. */
    private class RepositorioFake : ClienteDados {
        val itens = MutableStateFlow<List<ClienteEntity>>(emptyList())
        var simularCorrida = false

        override suspend fun inserir(cliente: ClienteEntity): Long {
            if (simularCorrida || itens.value.any { it.email == cliente.email }) {
                throw EmailDuplicadoException(IllegalStateException("UNIQUE constraint failed"))
            }
            val novo = cliente.copy(id = itens.value.size + 1)
            itens.value = listOf(novo) + itens.value
            return novo.id.toLong()
        }
        override fun listarTodos(): Flow<List<ClienteEntity>> = itens
        override suspend fun buscarPorId(id: Int) = itens.value.firstOrNull { it.id == id }
        override suspend fun buscarPorEmail(email: String) = itens.value.firstOrNull { it.email == email }
        override suspend fun excluir(id: Int) { itens.value = itens.value.filterNot { it.id == id } }
    }

    private val repo = RepositorioFake()
    private val service = ClienteServiceImpl(repo, Dispatchers.Unconfined)

    private fun Result<Long>.erro() = exceptionOrNull() as ErroDeValidacao

    @Test
    fun cadastroValido_apareceNaListagem() = runTest {
        val r = service.cadastrar("Ana Beatriz Souza", "ana.souza@exemplo.com")
        assertTrue(r.isSuccess)
        val lista = service.listar().first()
        assertEquals(1, lista.size)
        assertEquals("Ana Beatriz Souza", lista[0].nomeCompleto)
    }

    @Test
    fun nomeEEmail_saoNormalizados() = runTest {
        service.cadastrar("  Ana   Beatriz  Souza ", "  Ana.Souza@Exemplo.COM ")
        val c = service.listar().first()[0]
        assertEquals("Ana Beatriz Souza", c.nomeCompleto)
        assertEquals("ana.souza@exemplo.com", c.email)
    }

    @Test
    fun nomeVazioOuEmBranco_falhaNoCampoNome() = runTest {
        for (nome in listOf("", "   ")) {
            val r = service.cadastrar(nome, "ana@exemplo.com")
            assertEquals(Campo.NOME, r.erro().campo)
            assertEquals("Informe o nome completo.", r.erro().message)
        }
        assertTrue(repo.itens.value.isEmpty())
    }

    @Test
    fun emailForaDoFormato_falhaNoCampoEmail() = runTest {
        for (email in listOf("", "marina@", "marina", "@exemplo.com", "marina@exemplo", "ma rina@exemplo.com")) {
            val r = service.cadastrar("Marina Costa", email)
            assertEquals("e-mail \"$email\"", Campo.EMAIL, r.erro().campo)
        }
        assertTrue(repo.itens.value.isEmpty())
    }

    @Test
    fun emailRepetido_falhaSemDerrubarOApp() = runTest {
        service.cadastrar("Carlos Lima", "carlos.lima@exemplo.com")
        val r = service.cadastrar("Outro Carlos", "CARLOS.LIMA@exemplo.com")
        assertEquals(Campo.EMAIL, r.erro().campo)
        assertEquals("Este e-mail já está cadastrado.", r.erro().message)
        assertEquals(1, repo.itens.value.size)
    }

    @Test
    fun emailRepetidoDescobertoSoNaInsercao_tambemViraMensagem() = runTest {
        repo.simularCorrida = true
        val r = service.cadastrar("Carlos Lima", "carlos.lima@exemplo.com")
        assertEquals("Este e-mail já está cadastrado.", r.erro().message)
    }

    @Test
    fun listagem_trazOMaisNovoPrimeiro() = runTest {
        service.cadastrar("Ana Souza", "ana@exemplo.com")
        service.cadastrar("Bruno Lima", "bruno@exemplo.com")
        assertEquals(listOf("Bruno Lima", "Ana Souza"), service.listar().first().map { it.nomeCompleto })
    }

    @Test
    fun erroDeValidacao_continuaSendoIllegalArgumentException() = runTest {
        assertTrue(service.cadastrar("", "x").exceptionOrNull() is IllegalArgumentException)
    }
}
