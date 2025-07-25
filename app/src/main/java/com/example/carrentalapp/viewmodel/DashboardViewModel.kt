package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.carrentalapp.model.NavigationItem

class DashboardViewModel : ViewModel() {

    // Navigation items
    val navigationItems = listOf(
        NavigationItem("Home"),
        NavigationItem("Cars"),
        NavigationItem("Settings"),
        NavigationItem("Sign Out")
    )

    // Form state
    var location = mutableStateOf("")
    var pickupDate = mutableStateOf("24.03.2023")
    var pickupTime = mutableStateOf("18:00")
    var returnDate = mutableStateOf("28.03.2023")
    var returnTime = mutableStateOf("08:30")

    // Car categories with label and image URL
    val carCategories = listOf(
        "SUV" to "https://example.com/suv1.png",
        "SUV" to "https://example.com/suv2.png",
        "Sedan" to "https://example.com/sedan1.png",
        "Sedan" to "https://example.com/sedan2.png",
        "Offroad" to "https://example.com/offroad1.png",
        "Offroad" to "https://example.com/offroad2.png",
        "Truck" to "https://example.com/truck1.png",
        "Truck" to "https://example.com/truck2.png"
    )
}
