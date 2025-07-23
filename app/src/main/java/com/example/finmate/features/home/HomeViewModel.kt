import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finmate.features.home.TransactionRepository
import com.example.finmate.features.model.Transaction
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    private val _transactions = mutableStateOf<List<Transaction>>(emptyList())
    val transactions: State<List<Transaction>> = _transactions

    var netIncome by mutableStateOf(0.0)
        private set

    var totalIncome by mutableStateOf(0.0)
        private set

    var totalExpense by mutableStateOf(0.0)
        private set

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _transactions.value = transactionRepo.getRecentTransactions(5)
            netIncome = transactionRepo.getNetIncome()
            totalIncome = transactionRepo.getTotalIncome()
            totalExpense = transactionRepo.getTotalExpense()
        }
    }
}
