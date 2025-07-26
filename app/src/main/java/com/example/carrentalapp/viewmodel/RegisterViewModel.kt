package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.carrentalapp.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun registerUser(username: String, email: String, password: String, confirmPassword: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _message.value = "Please fill all fields"
            return
        }

        if (password != confirmPassword) {
            _message.value = "Passwords don't match"
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        val user = UserProfile(username, email)
                        FirebaseDatabase.getInstance().reference.child("users").child(uid).setValue(user)
                            .addOnSuccessListener {
                                _message.value = "Registration successful!"
                            }
                            .addOnFailureListener {
                                _message.value = "Failed to save user data"
                            }
                    }
                } else {
                    _message.value = task.exception?.message ?: "Registration failed"
                }
            }
    }

    fun clearMessage() {
        _message.value = ""
    }
}
