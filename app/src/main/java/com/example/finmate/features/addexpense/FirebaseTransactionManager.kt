package com.example.finmate.firebase

import android.util.Log
import com.example.finmate.features.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FirebaseTransactionManager {

    private val database = FirebaseDatabase.getInstance()

    fun saveTransaction(
        transaction: Transaction,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        val transactionId = UUID.randomUUID().toString()
        val transactionRef = database
            .getReference("transactions")
            .child(userId)
            .child(transactionId)

        transactionRef.setValue(transaction)
            .addOnSuccessListener {
                Log.d("FirebaseTransaction", "Transaction saved successfully.")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseTransaction", "Failed to save transaction", exception)
                onFailure(exception)
            }
    }
}
