package com.example.easyshop.service

/**
 * Erro de regra de negócio ligado a um campo do formulário.
 * A UI usa [campo] para mostrar a [message] embaixo do campo certo.
 * Estende [IllegalArgumentException] para continuar compatível com quem já testa por esse tipo.
 */
class ErroDeValidacao(val campo: Campo, mensagem: String) : IllegalArgumentException(mensagem) {
    enum class Campo { NOME, PRECO, EMAIL }
}
