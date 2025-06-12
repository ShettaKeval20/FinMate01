package com.example.finmate

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserInfoOnboardingScreen(onSubmit: () -> Unit) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser ?: return
    val database = FirebaseDatabase.getInstance().reference

    var step by remember { mutableStateOf(1) }

    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var incomeType by remember { mutableStateOf("") }
    var incomeExpanded by remember { mutableStateOf(false) }
    val incomeOptions = listOf("Salaried", "Business", "Freelance", "Student", "Passive Income", "Investment Income", "Pension", "Other")

    var country by remember { mutableStateOf(Locale.getDefault().displayCountry) }
    var currency by remember { mutableStateOf(Currency.getInstance(Locale.getDefault()).currencyCode) }
    var currencyExpanded by remember { mutableStateOf(false) }
    val currencyOptions = listOf("INR", "USD", "EUR", "CAD", "GBP", "JPY")

    val countryCurrencyList = remember {
        Locale.getISOCountries().mapNotNull { code ->
            try {
                val locale = Locale("", code)
                val countryName = locale.displayCountry
                val currencyCode = Currency.getInstance(locale).currencyCode
                val flag = getFlagEmoji(code)
                Triple(code, "$flag $countryName", "$flag $currencyCode")
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.second }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Step $step of 3", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(targetState = step, transitionSpec = {
            fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
        }) { targetStep ->
            when (targetStep) {
                1 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("What should we call you?") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("How old are you?") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                2 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("What's your income type?", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = incomeType,
                                onValueChange = {},
                                label = { Text("Income Type") },
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer( // invisible clickable overlay
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { incomeExpanded = true }
                            )
                            DropdownMenu(
                                expanded = incomeExpanded,
                                onDismissRequest = { incomeExpanded = false }
                            ) {
                                incomeOptions.forEach {
                                    DropdownMenuItem(onClick = {
                                        incomeType = it
                                        incomeExpanded = false
                                    }) {
                                        Text(it)
                                    }
                                }
                            }
                        }

                    }
                }
                3 -> {
                    var countryExpanded by remember { mutableStateOf(false) }
                    val countryList = remember {
                        Locale.getAvailableLocales()
                            .mapNotNull { it.displayCountry }
                            .filter { it.isNotBlank() }
                            .distinct()
                            .sorted()
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {

                        // Country Dropdown
                        Text("Where do you live?", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = country,
                                onValueChange = {},
                                label = { Text("Country") },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { countryExpanded = true }
                            )
                            DropdownMenu(
                                expanded = countryExpanded,
                                onDismissRequest = { countryExpanded = false }
                            ) {
                                countryList.forEach {
                                    DropdownMenuItem(onClick = {
                                        country = it
                                        countryExpanded = false

                                        // Try to fetch currency
                                        try {
                                            val locale = Locale("", Locale.getISOCountries().find { code ->
                                                Locale("", code).displayCountry == it
                                            } ?: "")
                                            currency = Currency.getInstance(locale).currencyCode
                                        } catch (_: Exception) {}
                                    }) {
                                        Text(it)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Currency Dropdown
                        Text("What's your preferred currency?", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = currency,
                                onValueChange = {},
                                label = { Text("Currency") },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { currencyExpanded = true }
                            )
                            DropdownMenu(
                                expanded = currencyExpanded,
                                onDismissRequest = { currencyExpanded = false }
                            ) {
                                val currencyList = listOf("INR", "USD", "EUR", "CAD", "GBP", "JPY", "AUD", "CHF", "CNY")
                                currencyList.forEach {
                                    DropdownMenuItem(onClick = {
                                        currency = it
                                        currencyExpanded = false
                                    }) {
                                        Text(it)
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (step > 1) {
                Button(onClick = { step-- }, modifier = Modifier.weight(1f)) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            Button(
                onClick = {
                    when (step) {
                        1 -> {
                            if (username.isBlank() || age.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            } else step++
                        }
                        2 -> {
                            if (incomeType.isBlank()) {
                                Toast.makeText(context, "Please select income type", Toast.LENGTH_SHORT).show()
                            } else step++
                        }
                        3 -> {
                            if (user != null && country.isNotBlank() && currency.isNotBlank()) {
                                val userInfo = mapOf(
                                    "username" to username,
                                    "age" to age,
                                    "incomeType" to incomeType,
                                    "country" to country,
                                    "currency" to currency
                                )
                                database.child("users").child(user.uid).setValue(userInfo)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Profile saved!", Toast.LENGTH_SHORT).show()
                                        onSubmit()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (step < 3) "Next" else "Continue")
            }
        }
    }
}

fun getFlagEmoji(countryCode: String): String {
    return countryCode
        .uppercase()
        .map { 0x1F1E6 - 'A'.code + it.code }
        .map { Character.toChars(it) }
        .joinToString("")
}
