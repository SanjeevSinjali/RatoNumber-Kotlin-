package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Your UserProfile data class (make sure it's imported or defined)
import com.example.carrentalapp.model.UserProfile

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> get() = _message

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> get() = _isAdmin

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = "Please enter email and password"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: ""
                    val userEmail = user?.email ?: ""

                    val isAdminUser = userEmail == "exampleadmin@gmail.com"

                    val userProfile = UserProfile(
                        username = if (isAdminUser) "Admin User" else "Normal User",
                        email = userEmail,
                        role = if (isAdminUser) "admin" else "user"
                    )

                    saveUserProfileRealtimeDB(userId, userProfile, isAdminUser) { success, msg ->
                        if (success) {
                            _message.value = if (isAdminUser) "Admin login successful!" else "Login successful!"
                            _isAdmin.value = isAdminUser
                        } else {
                            _message.value = "Login successful but failed to save profile: $msg"
                            _isAdmin.value = isAdminUser
                        }
                    }
                } else {
                    _message.value = task.exception?.localizedMessage ?: "Login failed"
                    _isAdmin.value = false
                }
            }
    }

    private fun saveUserProfileRealtimeDB(
        userId: String,
        profile: UserProfile,
        isAdmin: Boolean = false,
        onComplete: (Boolean, String) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance().reference
        val path = if (isAdmin) "admins" else "users"

        database.child(path).child(userId).setValue(profile)
            .addOnSuccessListener {
                onComplete(true, "Profile saved successfully")
            }
            .addOnFailureListener { e ->
                onComplete(false, "Failed to save profile: ${e.localizedMessage}")
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
        _isAdmin.value = false
    }
}
