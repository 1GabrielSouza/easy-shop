package com.example.easyshop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.easyshop.R

/** Títulos e a marca. */
val Bricolage = FontFamily(
    Font(R.font.bricolage_semibold, FontWeight.SemiBold),
    Font(R.font.bricolage_bold, FontWeight.Bold),
)

/** Texto corrido, rótulos e botões. */
val Figtree = FontFamily(
    Font(R.font.figtree_regular, FontWeight.Normal),
    Font(R.font.figtree_medium, FontWeight.Medium),
    Font(R.font.figtree_semibold, FontWeight.SemiBold),
)

/** Dados: preços, IDs, chaves e valores de atributos. */
val PlexMono = FontFamily(
    Font(R.font.plexmono_regular, FontWeight.Normal),
    Font(R.font.plexmono_medium, FontWeight.Medium),
)

/** Os estilos do design system mapeados para os slots do Material 3. */
val Tipografia = Typography(
    headlineLarge = TextStyle(            // titulo
        fontFamily = Bricolage, fontWeight = FontWeight.Bold,
        fontSize = 32.sp, lineHeight = 36.sp, letterSpacing = (-0.02).em,
    ),
    headlineSmall = TextStyle(            // subtitulo
        fontFamily = Bricolage, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = (-0.01).em,
    ),
    titleLarge = TextStyle(
        fontFamily = Bricolage, fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp, lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(              // titulo-card
        fontFamily = Bricolage, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Figtree, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(                // corpo
        fontFamily = Figtree, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(               // corpo-pequeno
        fontFamily = Figtree, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(                // legenda
        fontFamily = Figtree, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(               // rotulo
        fontFamily = Figtree, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Figtree, fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp, lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(               // chave (use em MAIÚSCULAS)
        fontFamily = PlexMono, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.04.em,
    ),
)

/** Estilo "dados": preço em destaque, ID do registro. */
val Dados = TextStyle(
    fontFamily = PlexMono, fontWeight = FontWeight.Medium,
    fontSize = 18.sp, lineHeight = 24.sp,
)

/** Estilo "dados-pequeno": valor de atributo, e-mail na lista. */
val DadosPequeno = TextStyle(
    fontFamily = PlexMono, fontWeight = FontWeight.Normal,
    fontSize = 13.sp, lineHeight = 18.sp,
)
