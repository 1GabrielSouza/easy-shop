package com.example.easyshop.ui.produtos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.easyshop.R
import com.example.easyshop.ui.componentes.EstadoVazio

/** Aba de produtos (NoSQL). Próximo passo: formulário com "+ Atributo" e a lista, via ProdutosViewModel. */
@Composable
fun ProdutosTela(modifier: Modifier = Modifier) {
    EstadoVazio(
        icone = R.drawable.ic_tag,
        titulo = "Nenhum produto ainda",
        texto = "Os produtos cadastrados aparecem aqui, cada um com os seus atributos.",
        modifier = modifier.fillMaxSize(),
    )
}
