package com.example.finmate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.finmate.util.PrefsHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashActivity : ComponentActivity() {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        scope.launch {
            delay(3000L) // Simulate splash delay

            val context = this@SplashActivity

            when {
                !PrefsHelper.isOnboardingCompleted(context) -> {
                    startActivity(Intent(context, OnboardingActivity::class.java))
                }
                FirebaseAuth.getInstance().currentUser?.isEmailVerified == true -> {
                    startActivity(Intent(context, MainActivity::class.java))
                }
                else -> {
                    startActivity(Intent(context, AuthActivity::class.java))
                }
            }

            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
