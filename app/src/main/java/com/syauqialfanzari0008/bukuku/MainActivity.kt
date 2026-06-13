package com.syauqialfanzari0008.bukuku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.syauqialfanzari0008.bukuku.ui.screen.MainScreen
import com.syauqialfanzari0008.bukuku.ui.theme.BukuKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BukuKuTheme {
                MainScreen()
            }
        }
    }
}