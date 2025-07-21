package com.example.finmate.utils

import android.util.Log
import com.example.finmate.features.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

fun saveTransactionToFirebase(transaction: Transaction, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId == null) {
        onFailure(Exception("User not logged in"))
        return
    }

    val database = FirebaseDatabase.getInstance()
    val transactionId = UUID.randomUUID().toString()

    val ref = database.getReference("users").child(userId).child("transactions").child(transactionId)
    ref.setValue(transaction)
        .addOnSuccessListener {
            Log.d("Firebase", "Transaction saved successfully")
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Failed to save transaction", exception)
            onFailure(exception)
        }
}
