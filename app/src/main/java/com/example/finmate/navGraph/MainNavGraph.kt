package com.example.finmate.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.finmate.features.scaffold.BottomBarScaffold

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main_tabs"
    ) {
        composable("main_tabs") {
            // Scaffold with BottomNav + BottomNavGraph inside
            BottomBarScaffold(navController)
        }

        // Outside tab-bar flow (fullscreen routes)
        composable("add_expense_flow") {

        }

        // Add more full screen pages here
    }
}
