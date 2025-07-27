package com.example.carrentalapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.carrentalapp.model.UserProfile
import com.google.firebase.database.FirebaseDatabase

class UserProfileViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference

    fun saveUserProfileRealtimeDB(
        userId: String,
        profile: UserProfile,
        isAdmin: Boolean = false,
        onComplete: (Boolean, String) -> Unit
    ) {
        val path = if (isAdmin) "admins" else "users"

        database.child(path).child(userId).setValue(profile)
            .addOnSuccessListener {
                onComplete(true, "Profile saved successfully")
            }
            .addOnFailureListener { e ->
                onComplete(false, "Failed to save profile: ${e.localizedMessage}")
            }
    }

    fun getUserProfileRealtimeDB(
        userId: String,
        isAdmin: Boolean = false,
        onResult: (UserProfile?) -> Unit
    ) {
        val path = if (isAdmin) "admins" else "users"

        database.child(path).child(userId).get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.getValue(UserProfile::class.java)
                onResult(profile)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun checkUserRole(userId: String, onResult: (String) -> Unit) {
        database.child("admins").child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    onResult("admin")
                } else {
                    onResult("user")
                }
            }
            .addOnFailureListener {
                onResult("user") // fallback default
            }
    }
}
