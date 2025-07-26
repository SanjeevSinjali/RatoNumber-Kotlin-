package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _message.value = "Email cannot be empty"
            _showDialog.value = true
            return
        }

        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = "Password reset email sent"
                    } else {
                        _message.value = task.exception?.localizedMessage ?: "Failed to send reset email"
                    }
                    _showDialog.value = true
                }
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
    }
}
