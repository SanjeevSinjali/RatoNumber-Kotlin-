// file: viewmodel/AdminDashboardViewModel.kt
package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow("Welcome Admin!")
    val uiState: StateFlow<String> = _uiState

    fun onAvailableCarsClicked() {
        // Add logic or navigation callback
    }

    fun onRentedCarsClicked() {
        // Add logic or navigation callback
    }

    fun onManageCarsClicked() {
        // Add logic or navigation callback
    }
}
