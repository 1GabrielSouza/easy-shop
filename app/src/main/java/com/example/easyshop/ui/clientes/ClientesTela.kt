package com.example.easyshop.ui.clientes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.easyshop.R
import com.example.easyshop.ui.componentes.EstadoVazio

/** Aba de clientes (SQL). Próximo passo: formulário de nome e e-mail e a lista, via ClientesViewModel. */
@Composable
fun ClientesTela(modifier: Modifier = Modifier) {
    EstadoVazio(
        icone = R.drawable.ic_user,
        titulo = "Nenhum cliente ainda",
        texto = "Os clientes cadastrados aparecem aqui, do mais novo para o mais antigo.",
        modifier = modifier.fillMaxSize(),
    )
}
