package com.example.finmate.features.addexpense

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finmate.features.model.Transaction
import com.example.finmate.features.model.TransactionCategory
import com.example.finmate.features.model.TransactionType
import com.example.finmate.features.model.suggestCategoryFromText
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
    var selectedDateTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val selectedCategory = remember { mutableStateOf<TransactionCategory?>(null) }
    val selectedSubCategory = remember { mutableStateOf<String?>(null) }

    val selectedType = remember { mutableStateOf(TransactionType.EXPENSE) }


    val availableCategories = TransactionCategory.getCategoriesByType(transactionType)
    val availableSubCategories = TransactionCategory.subCategoryMap[selectedCategory.value] ?: emptyList()

    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                Tab(selected = selectedTab == 0, onClick = {
                    selectedTab = 0
                    selectedCategory.value = null
                    selectedSubCategory.value = null
                }, text = { Text("Income") })
                Tab(selected = selectedTab == 1, onClick = {
                    selectedTab = 1
                    selectedCategory.value = null
                    selectedSubCategory.value = null
                }, text = { Text("Expense") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            InputCard(
                value = title,
                onValueChange = {
                    title = it

                    val parsed = parseNaturalTransactionInput(it)

                    parsed.amount?.let { amt -> amount = amt.toString() }
                    selectedCategory.value = parsed.category
                    selectedSubCategory.value = null // reset subcategory on title change
                    description = parsed.description

// Set tab based on type parsed
                    selectedTab = if (parsed.type == TransactionType.INCOME) 0 else 1

                },
                label = "Title",
                leadingIcon = Icons.Default.Title
            )


            selectedCategory.value?.let {
                Text(
                    text = "Suggested Category: ${it.label}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }


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
                selectedCategory = selectedCategory.value,
                onCategorySelected = {
                    selectedCategory.value = it
                    selectedSubCategory.value = null
                },
                transactionType = transactionType
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subcategory Dropdown
            // Horizontal Subcategory Chip Selector
            if (availableSubCategories.isNotEmpty()) {
                Text(
                    text = "Select Subcategory",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(availableSubCategories.size) { index ->
                        val subCategory = availableSubCategories[index]
                        val isSelected = selectedSubCategory.value == subCategory

                        Surface(
                            onClick = { selectedSubCategory.value = subCategory },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected)
                                BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                            else null,
                        ) {
                            Text(
                                text = subCategory,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                            category = selectedCategory.value?.label ?: "Others",
                            subCategory = selectedSubCategory.value ?: ""
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

data class ParsedTransaction(
    val amount: Double?,
    val category: TransactionCategory,
    val type: TransactionType,
    val title: String,
    val description: String
)

fun parseNaturalTransactionInput(input: String): ParsedTransaction {
    val lowercaseInput = input.lowercase()

    // Extract amount
    val amount = Regex("""\d+(\.\d+)?""").find(input)?.value?.toDoubleOrNull()

    // Determine type
    val type = if (listOf("received", "income", "salary", "freelance", "payment", "credited").any { it in lowercaseInput }) {
        TransactionType.INCOME
    } else {
        TransactionType.EXPENSE
    }

    // Determine category
    val category = suggestCategoryFromText(input)

    // Extract basic description (remove amount and keywords)
    val cleaned = input
        .replace(Regex("""\d+(\.\d+)?"""), "") // remove numbers
        .replace(Regex("""received|gifted|gave|credited|debited|payment|transfer|amount""", RegexOption.IGNORE_CASE), "") // remove common words
        .replace(Regex("""\s+"""), " ") // normalize whitespace
        .trim()


    val description = if (cleaned.length > 3) cleaned else category.label

    return ParsedTransaction(
        amount = amount,
        category = category,
        type = type,
        title = input,
        description = description
    )
}


@Composable
fun CategorySelector(
    selectedCategory: TransactionCategory?,
    onCategorySelected: (TransactionCategory) -> Unit,
    transactionType: TransactionType
) {
    val filteredCategories = TransactionCategory.getCategoriesByType(transactionType)

    Text(
        text = "Select Category",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    val chunked = filteredCategories.chunked(3)

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
                        onClick = {
                            onCategorySelected(category)
                        },
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
