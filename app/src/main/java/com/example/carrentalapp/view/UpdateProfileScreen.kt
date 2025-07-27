package com.example.carrentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrentalapp.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(viewModel: DashboardViewModel = viewModel()) {
    var name by remember { mutableStateOf(viewModel.userName.value) }
    var email by remember { mutableStateOf(viewModel.userEmail.value) }
    var phone by remember { mutableStateOf(viewModel.userPhone.value) }
    var password by remember { mutableStateOf(viewModel.userPassword.value) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Profile") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill out all fields")
                        }
                    } else {
                        val auth = FirebaseAuth.getInstance()
                        val currentUser = auth.currentUser
                        val userId = currentUser?.uid

                        if (currentUser != null && userId != null) {
                            // Update password
                            currentUser.updatePassword(password.trim())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Update profile data in Realtime Database
                                        val databaseRef = FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(userId)

                                        val userMap = mapOf(
                                            "name" to name.trim(),
                                            "email" to email.trim(),
                                            "phone" to phone.trim()
                                        )

                                        databaseRef.updateChildren(userMap)
                                            .addOnCompleteListener { dbTask ->
                                                if (dbTask.isSuccessful) {
                                                    // Update local ViewModel values
                                                    viewModel.userName.value = name.trim()
                                                    viewModel.userEmail.value = email.trim()
                                                    viewModel.userPhone.value = phone.trim()
                                                    viewModel.userPassword.value = password.trim()

                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Profile & password updated")
                                                    }
                                                } else {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Failed to update profile: ${dbTask.exception?.message}")
                                                    }
                                                }
                                            }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Password update failed: ${task.exception?.message}")
                                        }
                                    }
                                }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("User not logged in")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
