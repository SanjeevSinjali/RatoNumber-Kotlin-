package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> get() = _message

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = "Please enter email and password"
            return
        }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = "Login successful!"
                    } else {
                        _message.value = task.exception?.localizedMessage ?: "Login failed"
                    }
                }
        }
    }

    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            callback(false, "Email cannot be empty.")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Password reset email sent successfully.")
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Failed to send password reset email.")
                }
            }
    }

    fun clearMessage() {
        _message.value = ""
    }
}
