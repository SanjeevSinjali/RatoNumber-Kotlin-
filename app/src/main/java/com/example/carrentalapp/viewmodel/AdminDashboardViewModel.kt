package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminDashboardViewModel : ViewModel() {

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

    private val _uiState = MutableStateFlow("Welcome Admin!")
    val uiState: StateFlow<String> = _uiState

    val rentedCarsList = mutableStateListOf<RentedCar>()
    val availableCarsList = mutableStateListOf<AvailableCar>()

    fun onAvailableCarsClicked() {
        loadAllAvailableCars()
    }

    fun onRentedCarsClicked() {
        loadAllRentedCars()
    }

    fun onManageCarsClicked() {
        // Optionally add logic here
    }

    fun loadAllRentedCars() {
        val database = FirebaseDatabase.getInstance().reference.child("bookings")
        rentedCarsList.clear()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rentedCarsList.clear()
                for (childSnapshot in snapshot.children) {
                    val rentedCar = childSnapshot.getValue(RentedCar::class.java)
                    rentedCar?.let {
                        rentedCarsList.add(it.copy(key = childSnapshot.key ?: ""))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun loadAllAvailableCars() {
        val database = FirebaseDatabase.getInstance().reference.child("cars")
        availableCarsList.clear()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                availableCarsList.clear()
                for (childSnapshot in snapshot.children) {
                    val car = childSnapshot.getValue(AvailableCar::class.java)
                    val isRented = childSnapshot.child("isRented").getValue(Boolean::class.java) ?: false
                    if (!isRented) {
                        car?.let {
                            availableCarsList.add(it.copy(key = childSnapshot.key ?: ""))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

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

data class RentedCar(
    val car: String = "",
    val location: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    val returnDate: String = "",
    val returnTime: String = "",
    val price: String = "",
    val key: String = ""
)

data class AvailableCar(
    val car: String = "",
    val model: String = "",
    val location: String = "",
    val isRented: Boolean = false,
    val key: String = ""
)
