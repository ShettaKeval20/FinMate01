package com.example.finmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.finmate.NotificationScreen
import com.example.finmate.ProfileScreen
import com.example.finmate.SpendingHistoryScreen
import com.example.finmate.screens.*

@Composable
fun BottomNavGraph(navController: NavHostController,
                   modifier: Modifier = Modifier,
                   mainNavController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { HomeScreen(mainNavController)}
        composable(BottomNavItem.History.route) { SpendingHistoryScreen() }
        composable(BottomNavItem.Add.route) { SpendingHistoryScreen()}
//        composable(BottomNavItem.Add.route) { AddExpenseScreen(mainNavController = navController)}
        composable(BottomNavItem.Notifications.route) { NotificationScreen() }
        composable(BottomNavItem.Profile.route) { ProfileScreen() }
    }
}
