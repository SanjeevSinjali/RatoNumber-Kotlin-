package com.example.carrentalapp.model

import androidx.compose.ui.semantics.Role

data class UserProfile(
    val username: String = "",
    val email: String = "",
    val role: String = "user"
)
