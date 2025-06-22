package com.example.finmate.features.addexpense

import androidx.lifecycle.ViewModel
import com.example.finmate.features.model.Transaction
import com.example.finmate.features.state.AddExpenseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddExpenseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun updateDescription(desc: String) {
        _uiState.value = _uiState.value.copy(description = desc)
    }

    fun reset() {
        _uiState.value = AddExpenseUiState()
    }

    val currentTransaction: Transaction
        get() = Transaction(
            title = _uiState.value.title,
            amount = _uiState.value.amount.toDoubleOrNull() ?: 0.0,
            description = _uiState.value.description
        )
}
