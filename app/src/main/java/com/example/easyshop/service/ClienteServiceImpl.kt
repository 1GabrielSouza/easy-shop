package com.example.easyshop.service

import com.example.easyshop.data.local.ClienteEntity
import com.example.easyshop.data.repository.ClienteDados
import com.example.easyshop.data.repository.EmailDuplicadoException
import com.example.easyshop.service.ErroDeValidacao.Campo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Implementação real: normaliza e valida nome e e-mail, garante e-mail único e
 * delega a persistência ao repository em `Dispatchers.IO`.
 */
class ClienteServiceImpl(
    private val repository: ClienteDados,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ClienteService {

    override suspend fun cadastrar(nome: String, email: String): Result<Long> {
        val nomeLimpo = nome.trim().replace(Regex("\\s+"), " ")
        val emailLimpo = email.trim().lowercase()

        if (nomeLimpo.isEmpty()) {
            return falha(Campo.NOME, "Informe o nome completo.")
        }
        if (!FORMATO_EMAIL.matches(emailLimpo)) {
            return falha(Campo.EMAIL, "Digite um e-mail válido, como nome@exemplo.com.")
        }

        return withContext(dispatcher) {
            if (repository.buscarPorEmail(emailLimpo) != null) {
                return@withContext falha(Campo.EMAIL, MSG_DUPLICADO)
            }
            try {
                Result.success(repository.inserir(ClienteEntity(nomeCompleto = nomeLimpo, email = emailLimpo)))
            } catch (e: EmailDuplicadoException) {
                // Outro cadastro gravou o mesmo e-mail entre a consulta e a inserção.
                falha(Campo.EMAIL, MSG_DUPLICADO)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun listar(): Flow<List<ClienteEntity>> = repository.listarTodos()

    override suspend fun excluir(id: Int) = withContext(dispatcher) { repository.excluir(id) }

    private fun falha(campo: Campo, mensagem: String): Result<Long> =
        Result.failure(ErroDeValidacao(campo, mensagem))

    private companion object {
        const val MSG_DUPLICADO = "Este e-mail já está cadastrado."

        /** Algo@algo.dominio, sem espaços. Regex própria para rodar também nos testes da JVM. */
        val FORMATO_EMAIL = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    }
}
