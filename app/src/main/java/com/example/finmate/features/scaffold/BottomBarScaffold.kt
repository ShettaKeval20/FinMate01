// ✅ BottomBarScaffold.kt (Corrected & Working)
package com.example.finmate.features.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.finmate.navigation.BottomBar
import com.example.finmate.navigation.BottomNavGraph
import androidx.navigation.NavHostController

@Composable
fun BottomBarScaffold(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        BottomNavGraph(
            navController = bottomNavController,
            modifier = Modifier.padding(innerPadding),
            mainNavController = mainNavController // 👈 pass it
        )
    }
}

