package com.example.carrentalapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.carrentalapp.ui.theme.CarRentalAppTheme // adjust this if you have custom theme
import com.example.carrentalapp.viewmodel.RegisterViewModel

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarRentalAppTheme {
                RegisterScreen() // This now uses ViewModel internally
            }
        }
    }
}
