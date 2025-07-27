package com.example.carrentalapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                LoginScreen(
                    onSignUpClick = {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    },
                    onLoginSuccess = {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onAdminLoginSuccess = {
                        val intent = Intent(this, AdminDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}
