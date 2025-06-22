package com.example.finmate.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finmate.utils.AnalyticsHelper
import kotlinx.coroutines.launch

//@Composable
//fun HomeScreen() {
//    AnalyticsHelper.logScreenView("HomeScreen")
//
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Centered Text
//        Text(
//            text = "🏠 Home Screen",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.align(Alignment.Center)
//        )
//
//        // Floating Action Button
//        FloatingActionButton(
//            onClick = {
//                navController.navigate("add_expense_flow")
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Add Expense")
//        }
//
//        // Snackbar
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
//    }
//}

@Composable
fun HomeScreen(mainNavController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "🏠 Home Screen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Center)
        )

        FloatingActionButton(
            onClick = {
                mainNavController.navigate("add_expense_flow")
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }
    }
}

