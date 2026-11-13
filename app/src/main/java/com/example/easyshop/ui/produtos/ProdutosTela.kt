package com.example.easyshop.ui.produtos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easyshop.R
import com.example.easyshop.ServiceLocator
import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.ui.componentes.EstadoVazio
import com.example.easyshop.ui.componentes.TituloDaLista
import com.example.easyshop.ui.theme.EasyShopTheme

/** Aba Produtos (NoSQL). Liga o [ProdutosViewModel] à tela; o desenho fica em [ProdutosConteudo]. */
@Composable
fun ProdutosTela(
    modifier: Modifier = Modifier,
    viewModel: ProdutosViewModel = viewModel { ProdutosViewModel(ServiceLocator.produtoService) },
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    ProdutosConteudo(
        estado = estado,
        aoMudarNome = viewModel::aoMudarNome,
        aoMudarPreco = viewModel::aoMudarPreco,
        aoAdicionarAtributo = viewModel::adicionarAtributo,
        aoMudarChave = viewModel::aoMudarChave,
        aoMudarValor = viewModel::aoMudarValor,
        aoRemoverAtributo = viewModel::removerAtributo,
        aoSalvar = viewModel::salvar,
        aoAvisoExibido = viewModel::avisoExibido,
        modifier = modifier,
    )
}

/**
 * Só desenha o [estado] e repassa os cliques. Não conhece ViewModel nem banco,
 * por isso dá para ver na Preview com dados de exemplo.
 * A grade se adapta à largura: 1 coluna no celular, 2 ou 3 no tablet.
 */
@Composable
fun ProdutosConteudo(
    estado: ProdutosUiState,
    aoMudarNome: (String) -> Unit,
    aoMudarPreco: (String) -> Unit,
    aoAdicionarAtributo: () -> Unit,
    aoMudarChave: (Long, String) -> Unit,
    aoMudarValor: (Long, String) -> Unit,
    aoRemoverAtributo: (Long) -> Unit,
    aoSalvar: () -> Unit,
    aoAvisoExibido: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(estado.aviso) {
        val aviso = estado.aviso ?: return@LaunchedEffect
        snackbar.showSnackbar(aviso)
        aoAvisoExibido()
    }

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            // imePadding: o teclado não cobre o campo nem o botão Salvar.
            modifier = Modifier.widthIn(max = 1040.dp).fillMaxSize().imePadding(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "formulario", span = { GridItemSpan(maxLineSpan) }) {
                ProdutoFormulario(
                    estado = estado,
                    aoMudarNome = aoMudarNome,
                    aoMudarPreco = aoMudarPreco,
                    aoAdicionarAtributo = aoAdicionarAtributo,
                    aoMudarChave = aoMudarChave,
                    aoMudarValor = aoMudarValor,
                    aoRemoverAtributo = aoRemoverAtributo,
                    aoSalvar = aoSalvar,
                )
            }
            item(key = "titulo", span = { GridItemSpan(maxLineSpan) }) {
                TituloDaLista(
                    titulo = "Produtos cadastrados",
                    quantidade = estado.produtos.size,
                    singular = "item",
                    plural = "itens",
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
            when {
                estado.carregando -> item(key = "carregando", span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                estado.produtos.isEmpty() -> item(key = "vazio", span = { GridItemSpan(maxLineSpan) }) {
                    EstadoVazio(
                        icone = R.drawable.ic_tag,
                        titulo = "Nenhum produto ainda",
                        texto = "Os produtos cadastrados aparecem aqui, cada um com os seus atributos.",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                else -> items(estado.produtos, key = { it.id }) { produto ->
                    // animateItem: o produto novo entra no topo com animação.
                    ProdutoCard(produto, Modifier.animateItem())
                }
            }
        }

        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).imePadding())
    }
}

@Preview(name = "Produtos", showBackground = true, widthDp = 360, heightDp = 900)
@Composable
private fun ProdutosPreview() {
    EasyShopTheme {
        ProdutosConteudo(
            estado = ProdutosUiState(
                nome = "Camiseta básica",
                preco = "59,90",
                atributos = listOf(LinhaAtributo(0, "cor", "azul"), LinhaAtributo(1, "tamanho", "M")),
                produtos = listOf(
                    Produto(id = 2, nome = "Fone Bluetooth", preco = 199.9, atributos = mutableMapOf("cor" to "preto", "bateria" to "30 h")),
                    Produto(id = 1, nome = "Caneca de cerâmica", preco = 39.5),
                ),
                carregando = false,
            ),
            aoMudarNome = {},
            aoMudarPreco = {},
            aoAdicionarAtributo = {},
            aoMudarChave = { _, _ -> },
            aoMudarValor = { _, _ -> },
            aoRemoverAtributo = {},
            aoSalvar = {},
            aoAvisoExibido = {},
        )
    }
}
