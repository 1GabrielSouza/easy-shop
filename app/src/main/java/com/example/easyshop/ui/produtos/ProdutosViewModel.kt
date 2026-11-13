package com.example.easyshop.ui.produtos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyshop.service.ErroDeValidacao
import com.example.easyshop.service.ProdutoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Guarda o estado da aba Produtos e conversa com o [ProdutoService].
 * Não conhece o ObjectBox: só o Service. As regras (nome obrigatório, preço > 0,
 * limpeza dos atributos) ficam no Service, não aqui.
 */
class ProdutosViewModel(private val service: ProdutoService) : ViewModel() {

    private val _estado = MutableStateFlow(ProdutosUiState())
    val estado: StateFlow<ProdutosUiState> = _estado.asStateFlow()

    private var proximoIdDeLinha = 0L

    init {
        carregar()
    }

    fun aoMudarNome(texto: String) {
        _estado.update { it.copy(nome = texto, erroNome = null) }
    }

    fun aoMudarPreco(texto: String) {
        // Aceita só números e o separador decimal (vírgula ou ponto).
        val limpo = texto.filter { it.isDigit() || it == ',' || it == '.' }
        _estado.update { it.copy(preco = limpo, erroPreco = null) }
    }

    fun adicionarAtributo() {
        _estado.update { it.copy(atributos = it.atributos + LinhaAtributo(id = proximoIdDeLinha++)) }
    }

    fun aoMudarChave(id: Long, texto: String) = alterarLinha(id) { it.copy(chave = texto) }

    fun aoMudarValor(id: Long, texto: String) = alterarLinha(id) { it.copy(valor = texto) }

    fun removerAtributo(id: Long) {
        _estado.update { s -> s.copy(atributos = s.atributos.filterNot { it.id == id }) }
    }

    fun salvar() {
        val atual = _estado.value
        if (atual.salvando) return
        _estado.update { it.copy(salvando = true, erroNome = null, erroPreco = null) }

        // "12,90" vira 12.9. Vazio ou inválido vira NaN, e o Service recusa com a mensagem dele.
        val preco = atual.preco.replace(',', '.').toDoubleOrNull() ?: Double.NaN
        // As linhas viram o Map do documento. Espaços, chave vazia e repetida o Service limpa.
        val atributos = atual.atributos.associate { it.chave to it.valor }

        viewModelScope.launch {
            service.cadastrar(atual.nome, preco, atributos)
                .onSuccess {
                    _estado.update {
                        it.copy(
                            nome = "",
                            preco = "",
                            atributos = emptyList(),
                            salvando = false,
                            aviso = "Produto salvo.",
                        )
                    }
                    // O ObjectBox não avisa mudanças sozinho (o Room avisa): lemos a lista de novo.
                    carregar()
                }
                .onFailure { erro -> _estado.update { it.comErro(erro) } }
        }
    }

    fun avisoExibido() {
        _estado.update { it.copy(aviso = null) }
    }

    private fun carregar() {
        viewModelScope.launch {
            runCatching { service.listar() }
                .onSuccess { lista ->
                    _estado.update {
                        it.copy(produtos = lista.sortedByDescending { p -> p.id }, carregando = false)
                    }
                }
                .onFailure {
                    _estado.update { it.copy(carregando = false, aviso = "Não foi possível carregar os produtos.") }
                }
        }
    }

    private fun alterarLinha(id: Long, mudanca: (LinhaAtributo) -> LinhaAtributo) {
        _estado.update { s -> s.copy(atributos = s.atributos.map { if (it.id == id) mudanca(it) else it }) }
    }

    /** Erro de um campo vai para baixo do campo; qualquer outro vira aviso. */
    private fun ProdutosUiState.comErro(erro: Throwable): ProdutosUiState {
        val semSalvar = copy(salvando = false)
        return when ((erro as? ErroDeValidacao)?.campo) {
            ErroDeValidacao.Campo.NOME -> semSalvar.copy(erroNome = erro.message)
            ErroDeValidacao.Campo.PRECO -> semSalvar.copy(erroPreco = erro.message)
            else -> semSalvar.copy(
                aviso = (erro as? IllegalArgumentException)?.message ?: "Não foi possível salvar o produto.",
            )
        }
    }
}
