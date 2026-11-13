package com.example.easyshop.ui.produtos

import com.example.easyshop.data.nosql.Produto

/**
 * Uma linha do editor "+ Atributo".
 * É uma lista de linhas, e não um Map, porque duas linhas novas teriam a mesma chave vazia
 * e uma apagaria a outra. O [id] identifica a linha enquanto a chave é digitada.
 */
data class LinhaAtributo(
    val id: Long,
    val chave: String = "",
    val valor: String = "",
)

/** Tudo o que a aba Produtos mostra. A tela só desenha isto; quem muda o estado é o [ProdutosViewModel]. */
data class ProdutosUiState(
    // Formulário. O preço é texto porque, no meio da digitação, pode estar "" ou "12,".
    val nome: String = "",
    val preco: String = "",
    val atributos: List<LinhaAtributo> = emptyList(),

    // Erros de validação devolvidos pelo ProdutoService, mostrados embaixo do campo certo.
    val erroNome: String? = null,
    val erroPreco: String? = null,
    val salvando: Boolean = false,

    // Lista lida do ObjectBox (NoSQL), do mais novo para o mais antigo.
    val produtos: List<Produto> = emptyList(),
    val carregando: Boolean = true,

    // Mensagem rápida (snackbar). A tela avisa o ViewModel quando já mostrou.
    val aviso: String? = null,
)
