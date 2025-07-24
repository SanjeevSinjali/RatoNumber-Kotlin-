package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel

data class NavigationItem(val title: String)

class DashboardViewModel : ViewModel() {
    val navigationItems = listOf(
        NavigationItem("Home"),
        NavigationItem("Cars"),
        NavigationItem("Settings"),
        NavigationItem("Sign Out")
    )
}
