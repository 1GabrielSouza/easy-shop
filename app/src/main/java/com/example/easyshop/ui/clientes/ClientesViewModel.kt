package com.example.easyshop.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyshop.data.local.ClienteEntity
import com.example.easyshop.service.ClienteService
import com.example.easyshop.service.ErroDeValidacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Guarda o estado da aba Clientes e conversa com o [ClienteService].
 * Não conhece o Room: só o Service. As regras (nome, formato e unicidade do e-mail) ficam no Service.
 */
class ClientesViewModel(private val service: ClienteService) : ViewModel() {

    private val _estado = MutableStateFlow(ClientesUiState())
    val estado: StateFlow<ClientesUiState> = _estado.asStateFlow()

    init {
        // O Room devolve um Flow: a lista se atualiza sozinha a cada inserção ou exclusão.
        // (No ObjectBox, a aba Produtos precisa ler de novo depois de salvar.)
        service.listar()
            .onEach { lista -> _estado.update { it.copy(clientes = lista, carregando = false) } }
            .catch { _estado.update { it.copy(carregando = false, aviso = "Não foi possível carregar os clientes.") } }
            .launchIn(viewModelScope)
    }

    fun aoMudarNome(texto: String) {
        _estado.update { it.copy(nome = texto, erroNome = null) }
    }

    fun aoMudarEmail(texto: String) {
        _estado.update { it.copy(email = texto, erroEmail = null) }
    }

    fun salvar() {
        val atual = _estado.value
        if (atual.salvando) return
        _estado.update { it.copy(salvando = true, erroNome = null, erroEmail = null) }

        viewModelScope.launch {
            service.cadastrar(atual.nome, atual.email)
                .onSuccess {
                    _estado.update { it.copy(nome = "", email = "", salvando = false, aviso = "Cliente salvo.") }
                }
                .onFailure { erro -> _estado.update { it.comErro(erro) } }
        }
    }

    fun excluir(cliente: ClienteEntity) {
        viewModelScope.launch {
            runCatching { service.excluir(cliente.id) }
                .onSuccess { _estado.update { it.copy(aviso = "Cliente excluído.") } }
                .onFailure { _estado.update { it.copy(aviso = "Não foi possível excluir o cliente.") } }
        }
    }

    fun avisoExibido() {
        _estado.update { it.copy(aviso = null) }
    }

    /** Erro de um campo vai para baixo do campo; qualquer outro vira aviso. */
    private fun ClientesUiState.comErro(erro: Throwable): ClientesUiState {
        val semSalvar = copy(salvando = false)
        return when ((erro as? ErroDeValidacao)?.campo) {
            ErroDeValidacao.Campo.NOME -> semSalvar.copy(erroNome = erro.message)
            ErroDeValidacao.Campo.EMAIL -> semSalvar.copy(erroEmail = erro.message)
            else -> semSalvar.copy(
                aviso = (erro as? IllegalArgumentException)?.message ?: "Não foi possível salvar o cliente.",
            )
        }
    }
}
