package com.example.easyshop.ui.produtos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.ui.theme.Dados
import com.example.easyshop.ui.theme.DadosPequeno
import java.text.NumberFormat
import java.util.Locale

private val formatoReal: NumberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

/** 12.9 vira "R$ 12,90". */
fun formatarPreco(valor: Double): String = formatoReal.format(valor)

/** Cartão de um produto da lista: nome, ID, preço e as etiquetas de atributos do documento. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProdutoCard(produto: Produto, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = produto.nome,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "#${produto.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(formatarPreco(produto.preco), style = Dados, color = MaterialTheme.colorScheme.primary)
            }

            if (produto.atributos.isEmpty()) {
                Text(
                    text = "Sem atributos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    produto.atributos.forEach { (chave, valor) -> EtiquetaDeAtributo(chave, valor) }
                }
            }
        }
    }
}

/** Etiqueta "COR azul": chave em maiúsculas (fonte de dados) + valor. */
@Composable
private fun EtiquetaDeAtributo(chave: String, valor: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(chave.uppercase(), style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.width(6.dp))
            Text(valor, style = DadosPequeno, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
