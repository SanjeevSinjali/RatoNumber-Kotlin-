package com.example.carrentalapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.carrentalapp.ui.screens.ForgotPasswordScreen
import com.example.carrentalapp.viewmodel.ForgotPasswordViewModel

class ForgotPasswordActivity : ComponentActivity() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ForgotPasswordScreen(viewModel = viewModel)
            }
        }
    }
}
