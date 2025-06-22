package com.example.finmate.features.addexpense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseStep3_Overview(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var editableDesc by remember { mutableStateOf(state.description) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Step 3: Overview", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("Title: ${state.title}")
        Text("Amount: ₹${state.amount}")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = editableDesc,
            onValueChange = {
                editableDesc = it
                viewModel.updateDescription(it)
            },
            label = { Text("Description (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("Back") }
            Button(onClick = onSubmit) { Text("Submit") }
        }
    }
}
