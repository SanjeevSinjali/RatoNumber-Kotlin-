package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    // Add "Profile" to navigation items list
    val navigationItems = listOf("Home", "Cars", "Profile", "Sign Out")

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
        "Jeep" to "jeep",
        "Jeep" to "jeep1",
        "Pickup Truck" to "pickup",
        "Pickup Truck" to "pickup2"
    )

    // *** ADD THESE FOR PROFILE SCREEN ***

    var userName = mutableStateOf("")
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var userPassword = mutableStateOf("")
}