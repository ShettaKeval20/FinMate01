package com.example.finmate.features.home

data class User(
    val username: String = "",
    val currency: String = "₹", // Default symbol
    val email: String = "",
    val country: String = "",
    val incomeType: String = "",
    val age: String = "",
    val deviceId: String = ""
)