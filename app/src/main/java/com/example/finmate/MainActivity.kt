package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.finmate.navigation.BottomNavGraph
import com.example.finmate.ui.theme.FinMateTheme
import com.example.finmate.navigation.BottomBar
import androidx.compose.foundation.layout.fillMaxSize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinMateTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { innerPadding ->
                    BottomNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
