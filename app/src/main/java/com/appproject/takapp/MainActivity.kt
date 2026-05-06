package com.appproject.takapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.appproject.takapp.ui.NavGraph
import com.appproject.takapp.ui.theme.TakAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TakAppTheme {
                NavGraph()
            }
        }
    }
}