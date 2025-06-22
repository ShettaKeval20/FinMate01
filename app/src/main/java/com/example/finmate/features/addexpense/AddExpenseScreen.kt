package com.example.finmate.features.addexpense

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.finmate.features.model.TransactionViewModel

@Composable
fun AddExpenseScreen(
    mainNavController: NavHostController // 👈 Passed from MainNavGraph
) {
    val navController = rememberNavController()
    val viewModel: AddExpenseViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()

    NavHost(navController = navController, startDestination = "step_title") {
        composable("step_title") {
            AddExpenseStep1_Title(
                viewModel = viewModel,
                onNext = { navController.navigate("step_amount") }
            )
        }
        composable("step_amount") {
            AddExpenseStep2_Amount(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("step_overview") }
            )
        }
        composable("step_overview") {
            AddExpenseStep3_Overview(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSubmit = {
                    val transaction = viewModel.currentTransaction

                    transactionViewModel.saveTransaction(
                        transaction,
                        onSuccess = {
                            viewModel.reset()
                            // Pop AddExpense screen and return to Home
                            mainNavController.popBackStack()
                        },
                        onError = {
                            // TODO: Show snackbar if needed
                        }
                    )
                }
            )
        }
    }
}
