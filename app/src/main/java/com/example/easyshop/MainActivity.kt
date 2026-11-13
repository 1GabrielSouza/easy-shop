package com.example.easyshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.easyshop.ui.TelaPrincipal
import com.example.easyshop.ui.theme.EasyShopTheme

/** Única Activity do app. Toda a interface é Compose, a partir de [TelaPrincipal]. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyShopTheme {
                TelaPrincipal()
            }
        }
    }
}
