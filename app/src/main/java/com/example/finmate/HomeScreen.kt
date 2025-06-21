package com.example.finmate.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finmate.utils.AnalyticsHelper

@Composable
fun HomeScreen() {
    AnalyticsHelper.logScreenView("HomeScreen")
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("🏠 Home Screen", style = MaterialTheme.typography.headlineSmall)
    }
}
