package com.example.carrentalapp.model

data class RentedCar(
    val key: String = "",  // Firebase key
    val car: String = "",
    val location: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    val returnDate: String = "",
    val returnTime: String = "",
    val price : Int = 0,
    val timestamp: Long = 0L
)
