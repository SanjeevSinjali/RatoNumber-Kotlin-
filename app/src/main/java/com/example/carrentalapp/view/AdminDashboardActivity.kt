package com.example.carrentalapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carrentalapp.view.ui.theme.CarRentalAppTheme

class AdminDashboardActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarRentalAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text("Admin Dashboard") })
                    }
                ) { innerPadding ->
                    AdminDashboardContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AdminDashboardContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Admin!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* TODO: Navigate to Manage Cars screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Cars")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Navigate to Manage Rentals screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Rentals")
        }
    }
}
