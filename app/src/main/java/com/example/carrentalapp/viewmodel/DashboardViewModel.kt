package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class RentedCar(
    val key: String = "",    // Firebase key for the booking
    val userId: String = "",
    val car: String = "",
    val location: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    val returnDate: String = "",
    val returnTime: String = "",
    val timestamp: Long = 0L
)

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
        "SUV-1" to "suv",
        "SUV-2" to "suv1",
        "Sedan-1" to "sedan4",
        "Sedan-2" to "sedan2",
        "Jeep-1" to "jeep",
        "Jeep-2" to "jeep1",
        "Pickup Truck-1" to "pickup",
        "Pickup Truck-2" to "pickup2"
    )

    // Profile fields
    var userName = mutableStateOf("")
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var userPassword = mutableStateOf("")

    // List to hold rented cars fetched from Firebase
    val rentedCarsList = mutableStateListOf<RentedCar>()

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

    // 🔥 Load rented cars from Firebase (only for current user)
    fun loadRentedCars() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference.child("bookings")

        rentedCarsList.clear()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rentedCarsList.clear()
                for (childSnapshot in snapshot.children) {
                    val rentedCar = childSnapshot.getValue(RentedCar::class.java)
                    rentedCar?.let {
                        val carWithKey = it.copy(key = childSnapshot.key ?: "")
                        if (carWithKey.userId == userId) {
                            rentedCarsList.add(carWithKey)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here if needed
            }
        })
    }

    // 🔥 Cancel a rented car by removing booking from Firebase
    fun cancelRentedCar(key: String, onComplete: () -> Unit = {}) {
        if (key.isEmpty()) return

        val database = FirebaseDatabase.getInstance().reference.child("bookings").child(key)
        database.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                rentedCarsList.removeAll { it.key == key }
                onComplete()
            }
        }
    }
}
