package com.example.easyshop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Cada token vira um papel do Material 3, assim botões, campos e abas já saem com a cor certa.
// O salmão da marca não entra aqui: ele só aparece no logo.

private val EsquemaClaro = lightColorScheme(
    primary = Pinho700,
    onPrimary = Branco,
    primaryContainer = Pinho100,
    onPrimaryContainer = Pinho700,
    inversePrimary = Pinho300,
    secondary = Pinho700,
    onSecondary = Branco,
    secondaryContainer = Pinho100,
    onSecondaryContainer = Pinho700,
    tertiary = Salmao800,
    onTertiary = Branco,
    tertiaryContainer = Salmao100,
    onTertiaryContainer = Salmao800,
    error = Erro,
    onError = Branco,
    errorContainer = ErroFundo,
    onErrorContainer = Erro,
    background = Tinta100,
    onBackground = Tinta900,
    surface = Branco,
    onSurface = Tinta900,
    surfaceVariant = Tinta200,
    onSurfaceVariant = Tinta600,
    surfaceTint = Pinho700,
    inverseSurface = Tinta900,
    inverseOnSurface = Tinta100,
    outline = Tinta500,
    outlineVariant = Tinta300,
    scrim = Color(0x7A080F0C),
    surfaceBright = Branco,
    surfaceDim = Tinta200,
    surfaceContainerLowest = Branco,
    surfaceContainerLow = Branco,
    surfaceContainer = Branco,
    surfaceContainerHigh = Branco,
    surfaceContainerHighest = Tinta200,
)

private val EsquemaEscuro = darkColorScheme(
    primary = Pinho300,
    onPrimary = SobrePinhoEscuro,
    primaryContainer = Pinho900,
    onPrimaryContainer = Pinho300,
    inversePrimary = Pinho700,
    secondary = Pinho300,
    onSecondary = SobrePinhoEscuro,
    secondaryContainer = Pinho900,
    onSecondaryContainer = Pinho300,
    tertiary = Salmao200,
    onTertiary = Salmao900,
    tertiaryContainer = Salmao900,
    onTertiaryContainer = Salmao200,
    error = ErroEscuro,
    onError = ErroFundoEscuro,
    errorContainer = ErroFundoEscuro,
    onErrorContainer = ErroEscuro,
    background = FundoEscuro,
    onBackground = TextoEscuro,
    surface = SuperficieEscura,
    onSurface = TextoEscuro,
    surfaceVariant = RebaixadoEscuro,
    onSurfaceVariant = TextoSecundarioEscuro,
    surfaceTint = Pinho300,
    inverseSurface = TextoEscuro,
    inverseOnSurface = Tinta900,
    outline = BordaForteEscura,
    outlineVariant = BordaEscura,
    scrim = Color(0xA3000000),
    surfaceBright = SuperficieEscura,
    surfaceDim = FundoEscuro,
    surfaceContainerLowest = SuperficieEscura,
    surfaceContainerLow = SuperficieEscura,
    surfaceContainer = SuperficieEscura,
    surfaceContainerHigh = SuperficieEscura,
    surfaceContainerHighest = RebaixadoEscuro,
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),   // campos, chips de atributo
    medium = RoundedCornerShape(12.dp), // cards
    large = RoundedCornerShape(24.dp),  // folhas e diálogos
)

/**
 * Tema do app. Segue o modo claro/escuro do sistema.
 * Sem cor dinâmica do Android 12+: ela trocaria o pinho pela cor do papel de parede.
 */
@Composable
fun EasyShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
        typography = Tipografia,
        shapes = Formas,
        content = content,
    )
}
