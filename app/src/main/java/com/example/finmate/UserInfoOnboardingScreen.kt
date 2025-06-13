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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
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

    var country by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var countryExpanded by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    val countryCurrencyList = remember {
        Locale.getISOCountries().mapNotNull { code ->
            try {
                val locale = Locale("", code)
                val countryName = locale.displayCountry
                val currencyCode = Currency.getInstance(locale).currencyCode
                val flag = getFlagEmoji(code)
                CountryInfo(countryName, currencyCode, flag)
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.name }
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

                        var incomeTextFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = incomeType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Income Type") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        incomeTextFieldSize = coordinates.size.toSize()
                                    }
                                    .clickable { incomeExpanded = true }
                            )

                            DropdownMenu(
                                expanded = incomeExpanded,
                                onDismissRequest = { incomeExpanded = false },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { incomeTextFieldSize.width.toDp() })
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
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Where do you live?", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(12.dp))
                        var countryFieldSize by remember { mutableStateOf(IntSize.Zero) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = country,
                                onValueChange = {},
                                label = { Text("Country") },
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        countryFieldSize = coordinates.size
                                    }
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { countryExpanded = true }
                            )
                            DropdownMenu(
                                expanded = countryExpanded,
                                onDismissRequest = { countryExpanded = false },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { countryFieldSize.width.toDp() }),
                                properties = PopupProperties(focusable = false)
                            ) {
                                countryCurrencyList.forEach {
                                    DropdownMenuItem(onClick = {
                                        country = "${it.flag} ${it.name}"
                                        currency = it.currency
                                        countryExpanded = false
                                    }) {
                                        Text("${it.flag} ${it.name}")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("What's your preferred currency?", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(12.dp))
                        var currencyFieldSize by remember { mutableStateOf(IntSize.Zero) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = currency,
                                onValueChange = {},
                                label = { Text("Currency") },
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        currencyFieldSize = coordinates.size
                                    }
                            )
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { currencyExpanded = true }
                            )
                            DropdownMenu(
                                expanded = currencyExpanded,
                                onDismissRequest = { currencyExpanded = false },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { currencyFieldSize.width.toDp() }),
                                properties = PopupProperties(focusable = false)
                            ) {
                                countryCurrencyList.distinctBy { it.currency }.forEach {
                                    DropdownMenuItem(onClick = {
                                        currency = it.currency
                                        currencyExpanded = false
                                    }) {
                                        Text("${it.flag} ${it.currency}")
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

data class CountryInfo(val name: String, val currency: String, val flag: String)

fun getFlagEmoji(countryCode: String): String {
    return if (countryCode.length == 2) {
        val firstChar = Character.codePointAt(countryCode, 0) - 'A'.code + 0x1F1E6
        val secondChar = Character.codePointAt(countryCode, 1) - 'A'.code + 0x1F1E6
        String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    } else {
        "🏳️" // default fallback flag
    }
}
