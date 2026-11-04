package com.razegon.moroccoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.razegon.moroccoapp.ui.AndroidMoroccoApp
import com.razegon.shared.ui.theme.MoroccoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MoroccoAppTheme {
                AndroidMoroccoApp()
            }
        }
    }
}



