package com.example.easyshop.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Botão principal do formulário. Enquanto salva, fica desabilitado e mostra um indicador de progresso. */
@Composable
fun BotaoSalvar(
    texto: String,
    salvando: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = aoClicar,
        enabled = !salvando,
        modifier = modifier.fillMaxWidth().height(48.dp),
    ) {
        if (salvando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
            )
        } else {
            Text(texto)
        }
    }
}
