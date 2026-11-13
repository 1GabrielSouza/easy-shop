package com.example.easyshop.ui.componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Título de uma lista com a contagem à direita, por exemplo "Produtos cadastrados ... 3 ITENS". */
@Composable
fun TituloDaLista(
    titulo: String,
    quantidade: Int,
    singular: String,
    plural: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Text(
            text = "$quantidade ${if (quantidade == 1) singular else plural}".uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
