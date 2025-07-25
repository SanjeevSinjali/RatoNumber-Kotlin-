package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    // Navigation items — now plain strings
    val navigationItems = listOf("Home", "Cars", "Settings", "Sign Out")

    // Form state
    var location = mutableStateOf("")
    var pickupDate = mutableStateOf("24.03.2023")
    var pickupTime = mutableStateOf("18:00")
    var returnDate = mutableStateOf("28.03.2023")
    var returnTime = mutableStateOf("08:30")

    // Car categories (use resource name without extension for drawable)
    val carCategories = listOf(
        "SUV" to "suv",            // drawable/suv.jpg
        "SUV" to "suv1",           // drawable/suv1.jpg
        "Sedan" to "https://example.com/sedan1.png",
        "Sedan" to "https://example.com/sedan2.png",
        "Offroad" to "https://example.com/offroad1.png",
        "Offroad" to "https://example.com/offroad2.png",
        "Truck" to "https://example.com/truck1.png",
        "Truck" to "https://example.com/truck2.png"
    )
}
