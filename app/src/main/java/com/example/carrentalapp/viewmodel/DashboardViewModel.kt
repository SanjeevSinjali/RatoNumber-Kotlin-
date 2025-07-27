package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DashboardViewModel : ViewModel() {

    // Navigation
    val navigationItems = listOf("Home", "Cars", "Profile", "Rented Cars", "Sign Out")
    var selectedMenuItem = mutableStateOf("Home")

    // Car Booking Fields
    var location = mutableStateOf("")
    var pickupDate = mutableStateOf("24-03-2023")
    var pickupTime = mutableStateOf("18:00")
    var returnDate = mutableStateOf("28-03-2023")
    var returnTime = mutableStateOf("08:30")

    // Car selection tracking
    var selectedCar = mutableStateOf("")

    // Car list
    val carCategories = listOf(
        "SUV" to "suv",
        "SUV" to "suv1",
        "Sedan" to "sedan4",
        "Sedan" to "sedan2",
        "Jeep" to "jeep",
        "Jeep" to "jeep1",
        "Pickup Truck" to "pickup",
        "Pickup Truck" to "pickup2"
    )

    // Profile fields
    var userName = mutableStateOf("")
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var userPassword = mutableStateOf("")

    // 🔥 Send booking data to Firebase
    fun rentCarToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference
        val bookingId = database.child("bookings").push().key ?: return

        val bookingData = mapOf(
            "userId" to userId,
            "car" to selectedCar.value,
            "location" to location.value,
            "pickupDate" to pickupDate.value,
            "pickupTime" to pickupTime.value,
            "returnDate" to returnDate.value,
            "returnTime" to returnTime.value,
            "timestamp" to System.currentTimeMillis()
        )

        database.child("bookings").child(bookingId).setValue(bookingData)
    }
}
