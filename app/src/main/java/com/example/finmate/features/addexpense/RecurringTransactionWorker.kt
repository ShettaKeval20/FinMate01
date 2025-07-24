package com.example.finmate.features.addexpense

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finmate.features.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class RecurringTransactionWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return Result.failure()

        val dbRef = FirebaseDatabase.getInstance()
            .getReference("transactions")
            .child(uid)

        val snapshot = try {
            dbRef.get().await()
        } catch (e: Exception) {
            return Result.failure()
        }

        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val todayDate = formatter.format(Date())

        snapshot.children.forEach { transactionSnapshot ->
            val transaction = transactionSnapshot.getValue(Transaction::class.java)
                ?: return@forEach

            val lastDate = transaction.date
            val recurringType = transaction.recurring ?: "One Time"

            if (recurringType == "One Time") return@forEach

            if (shouldAddTransaction(recurringType, lastDate)) {
                val newTransaction = transaction.copy(date = todayDate)
                dbRef.push().setValue(newTransaction).await()
            }
        }

        return Result.success()
    }

    private fun shouldAddTransaction(recurring: String, lastDate: String?): Boolean {
        if (lastDate.isNullOrBlank()) return false

        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val last = try {
            formatter.parse(lastDate)
        } catch (e: Exception) {
            return false
        }

        val now = Calendar.getInstance()
        val then = Calendar.getInstance().apply { time = last }

        return when (recurring) {
            "Daily" -> now.get(Calendar.DAY_OF_YEAR) > then.get(Calendar.DAY_OF_YEAR)
                    || now.get(Calendar.YEAR) > then.get(Calendar.YEAR)

            "Weekly" -> now.get(Calendar.WEEK_OF_YEAR) > then.get(Calendar.WEEK_OF_YEAR)
                    || now.get(Calendar.YEAR) > then.get(Calendar.YEAR)

            "Monthly" -> now.get(Calendar.MONTH) > then.get(Calendar.MONTH)
                    || now.get(Calendar.YEAR) > then.get(Calendar.YEAR)

            else -> false
        }
    }
}
