package com.example.finmate.features.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.features.model.TransactionType

// Add this at the top with your imports
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (title: String, description: String, amount: Double, type: TransactionType, category: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedTabIndex by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }

    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Health", "Salary", "Others")
    val transactionType = if (selectedTabIndex == 0) TransactionType.INCOME else TransactionType.EXPENSE

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 550.dp, max = 750.dp),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        tonalElevation = 20.dp,
        scrimColor = Color.Black.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.98f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.4f))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Income", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = null) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Expense", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.MoneyOff, contentDescription = null) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Add ${transactionType.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            InputCard(
                value = title,
                onValueChange = {
                    title = it
                    showValidationError = false
                },
                label = "Title",
                leadingIcon = Icons.Default.Title,
                isError = showValidationError && title.isBlank(),
                errorText = if (showValidationError && title.isBlank()) "Title cannot be empty" else null
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputCard(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                leadingIcon = Icons.Default.Description,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputCard(
                value = amount,
                onValueChange = {
                    amount = it
                    showValidationError = false
                },
                label = "Amount",
                leadingIcon = if (transactionType == TransactionType.INCOME) Icons.Default.AttachMoney else Icons.Default.MoneyOff,
                isError = showValidationError && amount.toDoubleOrNull() == null,
                errorText = if (showValidationError && amount.toDoubleOrNull() == null) "Enter valid amount" else null
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .clip(RoundedCornerShape(14.dp))
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                category = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull()
                    if (title.isNotBlank() && parsedAmount != null && category.isNotBlank()) {
                        onSubmit(title, description, parsedAmount, transactionType, category)
                        onDismiss()
                    } else {
                        showValidationError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Save ${transactionType.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
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

