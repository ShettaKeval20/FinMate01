package com.example.finmate

import com.example.finmate.features.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TransactionRepository {

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun addTransaction(
        transaction: Transaction,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (userId != null) {
            val transactionId = dbRef.child("transactions").child(userId).push().key ?: return
            dbRef.child("transactions")
                .child(userId)
                .child(transactionId)
                .setValue(transaction)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }
}
