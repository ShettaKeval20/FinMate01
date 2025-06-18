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
import com.example.finmate.navigation.BottomNavGraph
import com.example.finmate.ui.theme.FinMateTheme
import com.example.finmate.navigation.BottomBar
import androidx.compose.foundation.layout.fillMaxSize
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ✅ Save device ID to Firebase
        saveDeviceIdToFirebase()

        setContent {
            FinMateTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { innerPadding ->
                    BottomNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
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
