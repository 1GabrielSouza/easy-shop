package com.example.easyshop.ui.componentes

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

/**
 * Campo de texto padrão do EasyShop: contorno, uma linha e mensagem de erro embaixo.
 * Quando [erro] não é nulo, o campo fica vermelho e mostra a mensagem.
 */
@Composable
fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    dica: String? = null,
    prefixo: String? = null,
    habilitado: Boolean = true,
    teclado: KeyboardOptions = KeyboardOptions.Default,
    acoesTeclado: KeyboardActions = KeyboardActions.Default,
    estilo: TextStyle = LocalTextStyle.current,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        modifier = modifier,
        enabled = habilitado,
        label = { Text(rotulo) },
        placeholder = if (dica == null) null else { { Text(dica) } },
        prefix = if (prefixo == null) null else { { Text(prefixo) } },
        supportingText = if (erro == null) null else { { Text(erro) } },
        isError = erro != null,
        singleLine = true,
        keyboardOptions = teclado,
        keyboardActions = acoesTeclado,
        textStyle = estilo,
        shape = MaterialTheme.shapes.small,
    )
}
