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
import com.example.finmate.features.addexpense.AddTransactionBottomSheet
import com.example.finmate.features.model.Transaction
import com.example.finmate.utils.AnalyticsHelper
import kotlinx.coroutines.launch
import com.example.finmate.utils.FirebaseUtils
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

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
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "🏠 Home Screen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Center)
        )

        FloatingActionButton(
            onClick = { showSheet = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        if (showSheet) {
            AddTransactionBottomSheet(
                onDismiss = { showSheet = false },
                onSubmit = { title, desc, amount, type, category ->
                    val transaction = Transaction(

                        title = title,
                        description = desc,
                        amount = amount,
                        date = System.currentTimeMillis(),
                        type = type,
                        category = category.name
                    )

                    FirebaseUtils.saveTransactionToFirebase(
                        transaction,
                        onSuccess = {
                            Toast.makeText(context, "Transaction saved ✅", Toast.LENGTH_SHORT).show()
                            showSheet = false
                        },
                        onFailure = {
                            Toast.makeText(context, "Failed to save ❌", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }
    }
}
