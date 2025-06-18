package com.example.finmate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.finmate.ui.theme.FinMateTheme
import com.example.finmate.utils.AnalyticsHelper

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.logEvent("main_screen_open")

        setContent {
            FinMateTheme {
                OnboardingScreen {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }
}
