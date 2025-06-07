package com.example.finmate.util

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private const val PREF_NAME = "finmate_prefs"
    private const val ONBOARDING_DONE = "onboarding_done"

    fun setOnboardingCompleted(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ONBOARDING_DONE, true)
            .apply()
    }

    fun isOnboardingCompleted(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(ONBOARDING_DONE, false)
    }
}

