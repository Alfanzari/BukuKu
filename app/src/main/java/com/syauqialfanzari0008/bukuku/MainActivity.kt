package com.syauqialfanzari0008.bukuku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil.Coil
import coil.ImageLoader
import com.syauqialfanzari0008.bukuku.network.client
import com.syauqialfanzari0008.bukuku.ui.screen.MainScreen
import com.syauqialfanzari0008.bukuku.ui.theme.BukuKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Coil.setImageLoader(
            ImageLoader.Builder(applicationContext)
                .okHttpClient(client)
                .build()
        )

        setContent {
            BukuKuTheme {
                MainScreen()
            }
        }
    }
}