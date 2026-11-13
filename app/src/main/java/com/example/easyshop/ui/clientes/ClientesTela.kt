package com.example.easyshop.ui.clientes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easyshop.R
import com.example.easyshop.ServiceLocator
import com.example.easyshop.data.local.ClienteEntity
import com.example.easyshop.ui.componentes.BotaoSalvar
import com.example.easyshop.ui.componentes.CampoTexto
import com.example.easyshop.ui.componentes.EstadoVazio
import com.example.easyshop.ui.componentes.SeloDoBanco
import com.example.easyshop.ui.componentes.TituloDaLista
import com.example.easyshop.ui.theme.DadosPequeno
import com.example.easyshop.ui.theme.EasyShopTheme
import com.example.easyshop.ui.theme.PlexMono

/** Aba Clientes (SQL). Liga o [ClientesViewModel] à tela; o desenho fica em [ClientesConteudo]. */
@Composable
fun ClientesTela(
    modifier: Modifier = Modifier,
    viewModel: ClientesViewModel = viewModel { ClientesViewModel(ServiceLocator.clienteService) },
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    ClientesConteudo(
        estado = estado,
        aoMudarNome = viewModel::aoMudarNome,
        aoMudarEmail = viewModel::aoMudarEmail,
        aoSalvar = viewModel::salvar,
        aoExcluir = viewModel::excluir,
        aoAvisoExibido = viewModel::avisoExibido,
        modifier = modifier,
    )
}

/** Só desenha o [estado] e repassa os cliques. Dá para ver na Preview com dados de exemplo. */
@Composable
fun ClientesConteudo(
    estado: ClientesUiState,
    aoMudarNome: (String) -> Unit,
    aoMudarEmail: (String) -> Unit,
    aoSalvar: () -> Unit,
    aoExcluir: (ClienteEntity) -> Unit,
    aoAvisoExibido: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    // Cliente esperando confirmação de exclusão. É estado só da tela, não precisa ir para o ViewModel.
    var paraExcluir by remember { mutableStateOf<ClienteEntity?>(null) }

    LaunchedEffect(estado.aviso) {
        val aviso = estado.aviso ?: return@LaunchedEffect
        snackbar.showSnackbar(aviso)
        aoAvisoExibido()
    }

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.widthIn(max = 1040.dp).fillMaxSize().imePadding(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "formulario", span = { GridItemSpan(maxLineSpan) }) {
                ClienteFormulario(
                    estado = estado,
                    aoMudarNome = aoMudarNome,
                    aoMudarEmail = aoMudarEmail,
                    aoSalvar = aoSalvar,
                )
            }
            item(key = "titulo", span = { GridItemSpan(maxLineSpan) }) {
                TituloDaLista(
                    titulo = "Clientes cadastrados",
                    quantidade = estado.clientes.size,
                    singular = "cliente",
                    plural = "clientes",
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
            when {
                estado.carregando -> item(key = "carregando", span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                estado.clientes.isEmpty() -> item(key = "vazio", span = { GridItemSpan(maxLineSpan) }) {
                    EstadoVazio(
                        icone = R.drawable.ic_user,
                        titulo = "Nenhum cliente ainda",
                        texto = "Os clientes cadastrados aparecem aqui, do mais novo para o mais antigo.",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                else -> items(estado.clientes, key = { it.id }) { cliente ->
                    ClienteCard(
                        cliente = cliente,
                        aoExcluir = { paraExcluir = cliente },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }

        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).imePadding())
    }

    paraExcluir?.let { cliente ->
        AlertDialog(
            onDismissRequest = { paraExcluir = null },
            title = { Text("Excluir cliente?") },
            text = { Text("${cliente.nomeCompleto} será removido do banco SQL. Não dá para desfazer.") },
            confirmButton = {
                TextButton(onClick = {
                    aoExcluir(cliente)
                    paraExcluir = null
                }) {
                    Text("Excluir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { paraExcluir = null }) { Text("Cancelar") }
            },
        )
    }
}

/** Formulário de novo cliente: nome completo e e-mail. */
@Composable
private fun ClienteFormulario(
    estado: ClientesUiState,
    aoMudarNome: (String) -> Unit,
    aoMudarEmail: (String) -> Unit,
    aoSalvar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habilitado = !estado.salvando

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(16.dp).animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Novo cliente", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                SeloDoBanco("SQL · Room")
            }

            CampoTexto(
                valor = estado.nome,
                aoMudar = aoMudarNome,
                rotulo = "Nome completo",
                erro = estado.erroNome,
                habilitado = habilitado,
                teclado = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            CampoTexto(
                valor = estado.email,
                aoMudar = aoMudarEmail,
                rotulo = "E-mail",
                dica = "nome@exemplo.com",
                erro = estado.erroEmail,
                habilitado = habilitado,
                teclado = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                acoesTeclado = KeyboardActions(onDone = { aoSalvar() }),
                estilo = LocalTextStyle.current.copy(fontFamily = PlexMono),
                modifier = Modifier.fillMaxWidth(),
            )

            BotaoSalvar(texto = "Salvar cliente", salvando = estado.salvando, aoClicar = aoSalvar)
        }
    }
}

/** Cartão de um cliente: iniciais, nome, e-mail, ID da linha na tabela e botão de excluir. */
@Composable
private fun ClienteCard(
    cliente: ClienteEntity,
    aoExcluir: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(iniciais(cliente.nomeCompleto), style = MaterialTheme.typography.labelLarge)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = cliente.nomeCompleto,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = cliente.email,
                    style = DadosPequeno,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = "#${cliente.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            IconButton(onClick = aoExcluir) {
                Icon(
                    painter = painterResource(R.drawable.ic_lixeira),
                    contentDescription = "Excluir ${cliente.nomeCompleto}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/** "Ana Beatriz Souza" vira "AS". */
private fun iniciais(nome: String): String {
    val partes = nome.split(' ').filter { it.isNotBlank() }
    val primeira = partes.firstOrNull()?.first() ?: return "?"
    val ultima = if (partes.size > 1) partes.last().first().toString() else ""
    return "$primeira$ultima".uppercase()
}

@Preview(name = "Clientes", showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun ClientesPreview() {
    EasyShopTheme {
        ClientesConteudo(
            estado = ClientesUiState(
                nome = "Marina Costa",
                email = "marina@",
                erroEmail = "Digite um e-mail válido, como nome@exemplo.com.",
                clientes = listOf(
                    ClienteEntity(id = 2, nomeCompleto = "Carlos Lima", email = "carlos.lima@exemplo.com"),
                    ClienteEntity(id = 1, nomeCompleto = "Ana Beatriz Souza", email = "ana.souza@exemplo.com"),
                ),
                carregando = false,
            ),
            aoMudarNome = {},
            aoMudarEmail = {},
            aoSalvar = {},
            aoExcluir = {},
            aoAvisoExibido = {},
        )
    }
}
