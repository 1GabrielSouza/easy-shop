package com.example.easyshop.ui.produtos

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.easyshop.R
import com.example.easyshop.ui.componentes.BotaoSalvar
import com.example.easyshop.ui.componentes.CampoTexto
import com.example.easyshop.ui.componentes.SeloDoBanco
import com.example.easyshop.ui.theme.PlexMono

/** Formulário de novo produto: nome, preço e o editor de atributos dinâmicos ("+ Atributo"). */
@Composable
fun ProdutoFormulario(
    estado: ProdutosUiState,
    aoMudarNome: (String) -> Unit,
    aoMudarPreco: (String) -> Unit,
    aoAdicionarAtributo: () -> Unit,
    aoMudarChave: (Long, String) -> Unit,
    aoMudarValor: (Long, String) -> Unit,
    aoRemoverAtributo: (Long) -> Unit,
    aoSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habilitado = !estado.salvando
    val estiloDados = LocalTextStyle.current.copy(fontFamily = PlexMono)

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            // animateContentSize: o cartão cresce suave quando entra ou sai uma linha de atributo.
            modifier = Modifier.padding(16.dp).animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Novo produto", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                SeloDoBanco("NoSQL · ObjectBox")
            }

            CampoTexto(
                valor = estado.nome,
                aoMudar = aoMudarNome,
                rotulo = "Nome",
                erro = estado.erroNome,
                habilitado = habilitado,
                teclado = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            CampoTexto(
                valor = estado.preco,
                aoMudar = aoMudarPreco,
                rotulo = "Preço",
                prefixo = "R$ ",
                dica = "0,00",
                erro = estado.erroPreco,
                habilitado = habilitado,
                teclado = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                acoesTeclado = KeyboardActions(onDone = { aoSalvar() }),
                estilo = estiloDados,
                modifier = Modifier.fillMaxWidth(),
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Atributos", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "Cada produto tem os seus: cor, tamanho, voltagem…",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                estado.atributos.forEach { linha ->
                    // key: ao remover uma linha do meio, cada campo continua ligado à sua linha.
                    key(linha.id) {
                        LinhaDeAtributo(
                            linha = linha,
                            aoMudarChave = aoMudarChave,
                            aoMudarValor = aoMudarValor,
                            aoRemover = aoRemoverAtributo,
                            habilitado = habilitado,
                            estilo = estiloDados,
                        )
                    }
                }

                TextButton(onClick = aoAdicionarAtributo, enabled = habilitado) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Atributo")
                }
            }

            BotaoSalvar(texto = "Salvar produto", salvando = estado.salvando, aoClicar = aoSalvar)
        }
    }
}

@Composable
private fun LinhaDeAtributo(
    linha: LinhaAtributo,
    aoMudarChave: (Long, String) -> Unit,
    aoMudarValor: (Long, String) -> Unit,
    aoRemover: (Long) -> Unit,
    habilitado: Boolean,
    estilo: TextStyle,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CampoTexto(
            valor = linha.chave,
            aoMudar = { aoMudarChave(linha.id, it) },
            rotulo = "Chave",
            dica = "cor",
            habilitado = habilitado,
            teclado = KeyboardOptions(imeAction = ImeAction.Next),
            estilo = estilo,
            modifier = Modifier.weight(1f),
        )
        CampoTexto(
            valor = linha.valor,
            aoMudar = { aoMudarValor(linha.id, it) },
            rotulo = "Valor",
            dica = "azul",
            habilitado = habilitado,
            teclado = KeyboardOptions(imeAction = ImeAction.Next),
            estilo = estilo,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { aoRemover(linha.id) }, enabled = habilitado) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Remover atributo",
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
