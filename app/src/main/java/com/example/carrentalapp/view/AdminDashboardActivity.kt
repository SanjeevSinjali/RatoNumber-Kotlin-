package com.example.carrentalapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.carrentalapp.view.ui.theme.CarRentalAppTheme
import com.example.carrentalapp.viewmodel.AdminDashboardViewModel

class AdminDashboardActivity : ComponentActivity() {

    private val viewModel: AdminDashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarRentalAppTheme {
                AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }
}
