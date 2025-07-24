package com.example.finmate.features.insight

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finmate.features.model.Transaction
import com.example.finmate.utils.FirebaseUtils

@Composable
fun SpendingHistoryScreen() {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        FirebaseUtils.fetchUserTransactions(
            onDataReceived = {
                transactions = it
                isLoading = false
            },
            onError = {
                error = it.message
                isLoading = false
            }
        )
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
        }
        else -> {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                items(transactions) { txn ->
                    TransactionCard(txn)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(transaction.title, style = MaterialTheme.typography.titleMedium)
            Text("₹ ${transaction.amount}", style = MaterialTheme.typography.bodyLarge)
            Text("Category: ${transaction.category}", style = MaterialTheme.typography.bodyMedium)
            Text("Type: ${transaction.type}", style = MaterialTheme.typography.bodySmall)
            Text("Date: ${transaction.date}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
