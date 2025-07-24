// utils/FirebaseUtils.kt
package com.example.finmate.utils

import com.example.finmate.features.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    fun saveTransactionToFirebase(transaction: Transaction, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onFailure(Exception("User not logged in"))
        val dbRef = FirebaseDatabase.getInstance().getReference("transactions").child(uid)
        val newKey = dbRef.push().key ?: return onFailure(Exception("Unable to generate key"))

        dbRef.child(newKey).setValue(transaction)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun fetchUserTransactions(onDataReceived: (List<Transaction>) -> Unit, onError: (Exception) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onError(Exception("User not logged in"))
        val dbRef = FirebaseDatabase.getInstance().getReference("transactions").child(uid)

        dbRef.get()
            .addOnSuccessListener { snapshot ->
                val transactions = mutableListOf<Transaction>()
                snapshot.children.forEach { child ->
                    val transaction = child.getValue(Transaction::class.java)
                    transaction?.let { transactions.add(it) }
                }
                onDataReceived(transactions)
            }
            .addOnFailureListener { onError(it) }
    }

}
