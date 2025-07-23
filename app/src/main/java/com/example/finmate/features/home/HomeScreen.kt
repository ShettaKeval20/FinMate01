
package com.example.finmate.screens

import HomeViewModel
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
import com.example.finmate.utils.FirebaseUtils
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    val formattedDate = formatter.format(Date())

                    val transaction = Transaction(
                        title = title,
                        description = desc,
                        amount = amount,
                        date = formattedDate,
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

//package com.example.finmate.screens
//
//import HomeViewModel
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.finmate.features.addexpense.AddTransactionBottomSheet
//import com.example.finmate.features.model.Transaction
//import com.example.finmate.utils.FirebaseUtils
//import java.text.NumberFormat
//import java.text.SimpleDateFormat
//import java.util.*
//
//private val green = Color(0xFF00C853)
//private val red = Color(0xFFD50000)
//
//@Composable
//fun HomeScreen(
//    mainNavController: NavHostController,
//    viewModel: HomeViewModel
//) {
//    val context = LocalContext.current
//    var showSheet by remember { mutableStateOf(false) }
//
//    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
//    val income = transactions.filter { it.type == "Income" }.sumOf { it.amount }
//    val expense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
//    val net = income - expense
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { showSheet = true },
//                containerColor = MaterialTheme.colorScheme.primary
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(horizontal = 20.dp, vertical = 16.dp)
//                .fillMaxSize()
//        ) {
//            Text(
//                text = "Welcome Back 👋",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            BalanceCard(net)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                AmountCard(title = "Income", amount = income, color = green)
//                AmountCard(title = "Expense", amount = expense, color = red)
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "Recent Transactions",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            LazyColumn(modifier = Modifier.weight(1f)) {
//                items(transactions.take(5)) { txn ->
//                    TransactionItem(txn)
//                }
//            }
//        }
//
//        if (showSheet) {
//            AddTransactionBottomSheet(
//                onDismiss = { showSheet = false },
//                onSubmit = { title, desc, amount, type, category ->
//                    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                    val formattedDate = formatter.format(Date())
//
//                    val transaction = Transaction(
//                        title = title,
//                        description = desc,
//                        amount = amount,
//                        date = formattedDate,
//                        type = type,
//                        category = category.name
//                    )
//                    FirebaseUtils.saveTransactionToFirebase(
//                        transaction,
//                        onSuccess = {
//                            Toast.makeText(context, "Transaction saved ✅", Toast.LENGTH_SHORT).show()
//                            showSheet = false
//                        },
//                        onFailure = {
//                            Toast.makeText(context, "Failed to save ❌", Toast.LENGTH_SHORT).show()
//                        }
//                    )
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun BalanceCard(net: Long) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(4.dp, RoundedCornerShape(20.dp)),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
//    ) {
//        Column(modifier = Modifier.padding(20.dp)) {
//            Text(text = "Total Balance", style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(
//                text = formatCurrency(net),
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold,
//                color = if (net >= 0) green else red
//            )
//        }
//    }
//}
//
//@Composable
//fun AmountCard(title: String, amount: Long, color: Color) {
//    Card(
//        modifier = Modifier
//            .weight(1f)
//            .padding(horizontal = 6.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Column(
//            Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = title, style = MaterialTheme.typography.bodyMedium)
//            Text(
//                text = formatCurrency(amount),
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = color
//            )
//        }
//    }
//}
//
//@Composable
//fun TransactionItem(transaction: Transaction) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        shape = RoundedCornerShape(14.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
//        elevation = CardDefaults.cardElevation(1.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column {
//                Text(transaction.title, style = MaterialTheme.typography.titleSmall)
//                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
//            }
//            Text(
//                text = (if (transaction.type == "Expense" ) "+ " else "- ") + formatCurrency(transaction.amount),
//                color = if (transaction.type == "Income") green else red,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}
//
//fun formatCurrency(amount: Long): String {
//    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
//}
