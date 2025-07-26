package com.example.carrentalapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.carrentalapp.viewmodel.ForgotPasswordViewModel
import com.example.carrentalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel) {
    var email by remember { mutableStateOf(TextFieldValue("")) }

    val message by viewModel.message.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("रातोNumber") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Red
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.forgot_password),
                contentDescription = null,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.resetPassword(email.text) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Send Reset Link")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDialog() },
                    title = {
                        Text(
                            text = if (message.contains("sent")) "Success" else "Error",
                            color = if (message.contains("sent")) Color.Green else Color.Red
                        )
                    },
                    text = { Text(text = message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.dismissDialog() }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
