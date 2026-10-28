package com.razegon.moroccoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.razegon.moroccoapp.ui.MoroccoApp
import com.razegon.shared.ui.theme.MoroccoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView)
            .hide(androidx.core.view.WindowInsetsCompat.Type.statusBars())

        setContent {
            MoroccoAppTheme {
                MoroccoApp()
            }
        }
    }
}



