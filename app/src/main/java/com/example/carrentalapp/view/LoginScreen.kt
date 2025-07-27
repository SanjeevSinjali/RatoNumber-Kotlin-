package com.example.carrentalapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrentalapp.R
import com.example.carrentalapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onAdminLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Forgot password states
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotPasswordEmail by remember { mutableStateOf("") }
    var forgotPasswordMessage by remember { mutableStateOf("") }
    var showForgotPasswordResultDialog by remember { mutableStateOf(false) }

    val message by viewModel.message.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            showDialog = true
            when (message) {
                "Login successful!" -> onLoginSuccess()
                "Admin login successful!" -> onAdminLoginSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("रातोNumber", color = Color.Red) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ratonumber_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(250.dp)
                    .width(600.dp)
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                        .clickable {
                            forgotPasswordEmail = ""
                            forgotPasswordMessage = ""
                            showForgotPasswordDialog = true
                        }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.loginUser(email, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Don't have an account? Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    }
                )
            }

            // Login result dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        viewModel.clearMessage()
                    },
                    title = {
                        Text(
                            text = if (message.contains("successful")) "Success" else "Error",
                            color = if (message.contains("successful")) Color.Green else Color.Red
                        )
                    },
                    text = { Text(text = message) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                viewModel.clearMessage()
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            // Forgot Password Dialog
            if (showForgotPasswordDialog) {
                AlertDialog(
                    onDismissRequest = { showForgotPasswordDialog = false },
                    title = { Text("Reset Password") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = forgotPasswordEmail,
                                onValueChange = { forgotPasswordEmail = it },
                                label = { Text("Enter your registered email") },
                                singleLine = true
                            )
                            if (forgotPasswordMessage.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = forgotPasswordMessage,
                                    color = if (forgotPasswordMessage.contains("successfully", ignoreCase = true)) Color.Green else Color.Red
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (forgotPasswordEmail.isNotBlank()) {
                                    viewModel.sendPasswordResetEmail(forgotPasswordEmail) { success, msg ->
                                        forgotPasswordMessage = msg
                                        if (success) {
                                            showForgotPasswordResultDialog = true
                                        }
                                    }
                                } else {
                                    forgotPasswordMessage = "Please enter an email."
                                }
                            }
                        ) {
                            Text("Send")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showForgotPasswordDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Forgot Password result dialog
            if (showForgotPasswordResultDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showForgotPasswordResultDialog = false
                        showForgotPasswordDialog = false
                    },
                    title = { Text("Reset Email Sent") },
                    text = { Text("Please check your email for password reset instructions.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showForgotPasswordResultDialog = false
                                showForgotPasswordDialog = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
