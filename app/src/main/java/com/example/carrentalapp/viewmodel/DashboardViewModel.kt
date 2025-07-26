package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val navigationItems = listOf("Home", "Cars", "Settings", "Sign Out")

    var selectedMenuItem = mutableStateOf("Home")

    var location = mutableStateOf("")
    var pickupDate = mutableStateOf("24-03-2023")
    var pickupTime = mutableStateOf("18:00")
    var returnDate = mutableStateOf("28-03-2023")
    var returnTime = mutableStateOf("08:30")

    val carCategories = listOf(
        "SUV" to "suv",            // drawable/suv.jpg
        "SUV" to "suv1",           // drawable/suv1.jpg
        "Sedan" to "sedan4",
        "Sedan" to "sedan2",
        "EV" to "ev",
        "EV" to "ev1",
        "Pickup Truck" to "pickup",
        "Pickup Truck" to "pickup2"
    )
}
