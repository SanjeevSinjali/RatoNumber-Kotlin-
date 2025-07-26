package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun resetPassword(email: String, currentPassword: String, newPassword: String, confirmPassword: String) {
        val user = auth.currentUser
        if (user == null) {
            _message.value = "No logged-in user found"
            _showDialog.value = true
            return
        }

        if (email.isBlank() || currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            _message.value = "Please fill all fields"
            _showDialog.value = true
            return
        }

        if (newPassword != confirmPassword) {
            _message.value = "New passwords do not match"
            _showDialog.value = true
            return
        }

        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        _message.value = "Password successfully updated"
                    } else {
                        _message.value = "Failed to update password: ${updateTask.exception?.localizedMessage}"
                    }
                    _showDialog.value = true
                }
            } else {
                _message.value = "Re-authentication failed: ${reauthTask.exception?.localizedMessage}"
                _showDialog.value = true
            }
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
    }
}
