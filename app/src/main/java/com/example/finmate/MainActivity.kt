package com.example.finmate

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.finmate.ui.theme.FinMateTheme
import com.example.finmate.navigation.BottomBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.finmate.features.addexpense.RecurringTransactionWorker
import com.example.finmate.navGraph.MainNavGraph
import com.example.finmate.navigation.BottomNavGraph
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ✅ Save device ID to Firebase
        saveDeviceIdToFirebase()

        val request = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(1, TimeUnit.DAYS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "RecurringTransactionWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )


        setContent {
            FinMateTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    MainNavGraph(navController = navController)
                }
            }
        }
    }


    private fun saveDeviceIdToFirebase() {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val email = currentUser.email ?: "NoEmail"
            val userRef = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(currentUser.uid)

            val userInfo = mapOf(
                "deviceId" to deviceId,
                "email" to email
            )

            userRef.updateChildren(userInfo)
                .addOnSuccessListener {
                    Log.d("MainActivity", "✅ Device ID & Email saved successfully")
                }
                .addOnFailureListener {
                    Log.e("MainActivity", "❌ Failed to save data: ${it.message}")
                }
        } else {
            Log.w("MainActivity", "⚠️ User not logged in, skipping data upload")
        }
    }
}
