package com.example.carrentalapp.model

data class NavigationItem(
    val title : String
)

data class Car(
    val id: Int,
    val name: String,
    val type: String,           // SUV, Sedan, Truck, Off-road
    val imageUrl: String,
    val seats: Int,
    val pricePerDay: Double,
    val isAvailable: Boolean
)
