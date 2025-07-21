package com.example.finmate.features.addexpense

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finmate.features.model.Transaction
import com.example.finmate.features.model.TransactionCategory
import com.example.finmate.features.model.TransactionType
import com.example.finmate.firebase.FirebaseTransactionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String, String, Double, TransactionType, TransactionCategory) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableStateOf(0) } // 0 = Income, 1 = Expense
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val transactionType = if (selectedTab == 0) TransactionType.INCOME else TransactionType.EXPENSE
    val availableCategories = TransactionCategory.getCategoriesByType(transactionType)
    var selectedCategory by remember { mutableStateOf(availableCategories.first()) }

    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 250.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Income") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Expense") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            InputCard(
                value = title,
                onValueChange = { title = it },
                label = "Title",
                leadingIcon = Icons.Default.Title
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputCard(
                value = amount,
                onValueChange = { amount = it },
                label = "Amount",
                leadingIcon = Icons.Default.AttachMoney
            )

            Spacer(modifier = Modifier.height(20.dp))

            InputCard(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                leadingIcon = Icons.Default.Description
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Selector
            CategorySelector(
                categories = availableCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull()
                    if (!title.isBlank() && parsedAmount != null) {
                        val transaction = Transaction(
                            title = title,
                            description = description,
                            amount = parsedAmount,
                            date = System.currentTimeMillis(),
                            type = transactionType,
                            category = selectedCategory.name
                        )

                        FirebaseTransactionManager.saveTransaction(
                            transaction,
                            onSuccess = {
                                Log.d("Firebase", "Transaction saved successfully.")
                                onDismiss()
                            },
                            onFailure = {
                                Log.e("Firebase", "Failed to save transaction: ${it.message}")
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save ${transactionType.name.lowercase().replaceFirstChar { it.uppercase() }}")
            }
        }
    }
}

@Composable
fun CategorySelector(
    categories: List<TransactionCategory>,
    selectedCategory: TransactionCategory,
    onCategorySelected: (TransactionCategory) -> Unit
) {
    Text(
        text = "Select Category",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    val chunked = categories.chunked(3)

    Column(modifier = Modifier.fillMaxWidth()) {
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { category ->
                    val isSelected = selectedCategory == category
                    val background = if (isSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant

                    Surface(
                        onClick = { onCategorySelected(category) },
                        shape = RoundedCornerShape(14.dp),
                        color = background,
                        border = if (isSelected)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else null,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = category.label,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun InputCard(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isError: Boolean = false,
    errorText: String? = null,
    maxLines: Int = 1
) {
    val keyboardType = when (label.lowercase()) {
        "amount" -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(leadingIcon, contentDescription = label) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)),
            singleLine = maxLines == 1,
            maxLines = maxLines,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                errorLabelColor = MaterialTheme.colorScheme.error
            )
        )
        if (isError && errorText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}
