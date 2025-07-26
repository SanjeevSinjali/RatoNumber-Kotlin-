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

    fun clearMessage() {
        _message.value = ""
    }
}
