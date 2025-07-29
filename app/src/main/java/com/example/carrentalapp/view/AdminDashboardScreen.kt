// file: view/AdminDashboardScreen.kt
package com.example.carrentalapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carrentalapp.viewmodel.AdminDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: AdminDashboardViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "रातोNumber", color = Color.Red)
                }
            )
        }
    ) { innerPadding ->
        AdminDashboardContent(
            modifier = Modifier.padding(innerPadding),
            uiText = viewModel.uiState.collectAsState().value,
            onAvailableCarsClick = { viewModel.onAvailableCarsClicked() },
            onRentedCarsClick = { viewModel.onRentedCarsClicked() },
            onManageCarsClick = { viewModel.onManageCarsClicked() }
        )
    }
}

@Composable
fun AdminDashboardContent(
    modifier: Modifier = Modifier,
    uiText: String,
    onAvailableCarsClick: () -> Unit,
    onRentedCarsClick: () -> Unit,
    onManageCarsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = uiText, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAvailableCarsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Available Cars")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRentedCarsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Rented Cars")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onManageCarsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Cars")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboardContent(
        uiText = "Welcome Admin!",
        onAvailableCarsClick = {},
        onRentedCarsClick = {},
        onManageCarsClick = {}
    )
}
