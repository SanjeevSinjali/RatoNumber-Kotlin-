package com.example.carrentalapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class RentedCar(
    val key: String = "",
    val userId: String = "",
    val car: String = "",
    val location: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    val returnDate: String = "",
    val returnTime: String = "",
    val price: Int = 0,
    val timestamp: Long = 0L
)

class DashboardViewModel : ViewModel() {

    val navigationItems = listOf("Home", "Cars", "Profile", "Rented Cars", "Sign Out")
    var selectedMenuItem = mutableStateOf("Home")

    var location = mutableStateOf("")
    var pickupDate = mutableStateOf("24-03-2023")
    var pickupTime = mutableStateOf("18:00")
    var returnDate = mutableStateOf("28-03-2023")
    var returnTime = mutableStateOf("08:30")
    var selectedCar = mutableStateOf("")

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

    var userName = mutableStateOf("")
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var userPassword = mutableStateOf("")

    val rentedCarsList = mutableStateListOf<RentedCar>()

    // ✅ Send booking data to Firebase
    fun rentCarToFirebase(onSuccess: () -> Unit, onUnavailable: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference

        database.child("bookings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isCarAvailable = true
                for (bookingSnapshot in snapshot.children) {
                    val booking = bookingSnapshot.getValue(RentedCar::class.java)
                    if (booking != null && booking.car == selectedCar.value) {
                        isCarAvailable = false
                        break
                    }
                }

                if (!isCarAvailable) {
                    onUnavailable()
                    return
                }

                val bookingId = database.child("bookings").push().key ?: return

                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
                val pickup = try { dateFormat.parse(pickupDate.value) } catch (e: Exception) { null }
                val returnD = try { dateFormat.parse(returnDate.value) } catch (e: Exception) { null }

                val days = if (pickup != null && returnD != null) {
                    val diff = returnD.time - pickup.time
                    (diff / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
                } else {
                    1
                }

                val totalCost = days * 1500

                val bookingData = mapOf(
                    "userId" to userId,
                    "car" to selectedCar.value,
                    "location" to location.value,
                    "pickupDate" to pickupDate.value,
                    "pickupTime" to pickupTime.value,
                    "returnDate" to returnDate.value,
                    "returnTime" to returnTime.value,
                    "price" to totalCost,
                    "timestamp" to System.currentTimeMillis()
                )

                database.child("bookings").child(bookingId).setValue(bookingData).addOnSuccessListener {
                    onSuccess()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ✅ Load rented cars from Firebase
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

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ✅ Cancel a rented car
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
