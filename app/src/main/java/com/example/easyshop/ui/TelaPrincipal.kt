package com.example.easyshop.ui

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.easyshop.R
import com.example.easyshop.ui.clientes.ClientesTela
import com.example.easyshop.ui.produtos.ProdutosTela
import com.example.easyshop.ui.theme.Bricolage
import com.example.easyshop.ui.theme.EasyShopTheme

/** As duas abas do app. Cada uma mostra de qual banco os dados vêm. */
enum class Aba(val rotulo: String, val banco: String, @DrawableRes val icone: Int) {
    PRODUTOS("Produtos", "NoSQL · ObjectBox", R.drawable.ic_tag),
    CLIENTES("Clientes", "SQL · Room", R.drawable.ic_user),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPrincipal() {
    // rememberSaveable: a aba escolhida sobrevive a girar a tela.
    var abaAtual by rememberSaveable { mutableStateOf(Aba.PRODUTOS) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Marca() })
                PrimaryTabRow(selectedTabIndex = abaAtual.ordinal) {
                    Aba.entries.forEach { aba ->
                        Tab(
                            selected = aba == abaAtual,
                            onClick = { abaAtual = aba },
                            modifier = Modifier.height(60.dp),
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ) {
                            RotuloDaAba(aba)
                        }
                    }
                }
            }
        },
    ) { espaco ->
        val conteudo = Modifier.padding(espaco)
        when (abaAtual) {
            Aba.PRODUTOS -> ProdutosTela(conteudo)
            Aba.CLIENTES -> ClientesTela(conteudo)
        }
    }
}

/** Símbolo + nome, como no lockup do logo. O símbolo troca de cor sozinho no modo escuro (drawable-night). */
@Composable
private fun Marca() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.ic_easyshop_simbolo),
            contentDescription = null,
            modifier = Modifier.size(width = 34.dp, height = 24.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "easyshop",
            fontFamily = Bricolage,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            letterSpacing = (-0.02).em,
        )
    }
}

/** Ícone + nome da aba e, embaixo, o banco em fonte de dados. A cor vem do Tab (selecionada ou não). */
@Composable
private fun RotuloDaAba(aba: Aba) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(aba.icone),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(aba.rotulo, style = MaterialTheme.typography.labelLarge)
    }
    Text(
        text = aba.banco.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, lineHeight = 14.sp),
    )
}

@Preview(name = "Claro", showBackground = true, widthDp = 360, heightDp = 640)
@Preview(
    name = "Escuro", showBackground = true, widthDp = 360, heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun TelaPrincipalPreview() {
    EasyShopTheme { TelaPrincipal() }
}
