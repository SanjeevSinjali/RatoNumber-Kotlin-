package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.carrentalapp.model.NavigationItem // ✅ Import the correct one

class DashboardViewModel : ViewModel() {
    val navigationItems = listOf(
        NavigationItem("Home"),
        NavigationItem("Cars"),
        NavigationItem("Settings"),
        NavigationItem("Sign Out")
    )
}
