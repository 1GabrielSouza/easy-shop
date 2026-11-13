package com.example.easyshop.ui.clientes

import com.example.easyshop.data.local.ClienteEntity

/** Tudo o que a aba Clientes mostra. A tela só desenha isto; quem muda o estado é o [ClientesViewModel]. */
data class ClientesUiState(
    // Formulário.
    val nome: String = "",
    val email: String = "",

    // Erros de validação devolvidos pelo ClienteService, mostrados embaixo do campo certo.
    val erroNome: String? = null,
    val erroEmail: String? = null,
    val salvando: Boolean = false,

    // Lista lida do Room (SQL), do mais novo para o mais antigo.
    val clientes: List<ClienteEntity> = emptyList(),
    val carregando: Boolean = true,

    // Mensagem rápida (snackbar). A tela avisa o ViewModel quando já mostrou.
    val aviso: String? = null,
)
