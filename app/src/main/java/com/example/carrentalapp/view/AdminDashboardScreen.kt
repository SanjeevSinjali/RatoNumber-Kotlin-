package com.example.carrentalapp.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.carrentalapp.viewmodel.AdminDashboardViewModel
import com.example.carrentalapp.viewmodel.RentedCar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: AdminDashboardViewModel) {
    var showRentedCars by remember { mutableStateOf(false) }
    var showManageCars by remember { mutableStateOf(false) }
    var showAvailableCars by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
        when {
            showRentedCars -> {
                RentedCarsListScreen(
                    rentedCars = viewModel.rentedCarsList,
                    onCancel = { key ->
                        viewModel.cancelRentedCar(key) {
                            Toast.makeText(context, "Rental cancelled", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onBackClick = { showRentedCars = false },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            showManageCars -> {
                ManageCarsScreen(
                    viewModel = viewModel,
                    onBackClick = { showManageCars = false },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            showAvailableCars -> {
                AvailableCarsScreen(
                    viewModel = viewModel,
                    onBackClick = { showAvailableCars = false },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                AdminDashboardContent(
                    modifier = Modifier.padding(innerPadding),
                    uiText = viewModel.uiState.collectAsState().value,
                    onAvailableCarsClick = { showAvailableCars = true },
                    onRentedCarsClick = {
                        showRentedCars = true
                        viewModel.onRentedCarsClicked()
                    },
                    onManageCarsClick = { showManageCars = true }
                )
            }
        }
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

        Button(onClick = onAvailableCarsClick, modifier = Modifier.fillMaxWidth()) {
            Text("Available Cars")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRentedCarsClick, modifier = Modifier.fillMaxWidth()) {
            Text("Rented Cars")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onManageCarsClick, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Cars")
        }
    }
}

@Composable
fun AvailableCarsScreen(
    viewModel: AdminDashboardViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Button(onClick = onBackClick) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Available Cars", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        viewModel.carCategories.forEach { (label, imageUrl) ->
            CarImageCardReadOnly(
                label = label,
                imageUrl = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
fun RentedCarsListScreen(
    rentedCars: List<RentedCar>,
    onCancel: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Button(onClick = onBackClick) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Rented Cars", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        if (rentedCars.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No rented cars found")
            }
        } else {
            rentedCars.forEach { rentedCar ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Car: ${rentedCar.car}")
                        Text("Location: ${rentedCar.location}")
                        Text("Pick-up: ${rentedCar.pickupDate} at ${rentedCar.pickupTime}")
                        Text("Return: ${rentedCar.returnDate} at ${rentedCar.returnTime}")
                        Text("Price: Rs. ${rentedCar.price}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onCancel(rentedCar.key) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageCarsScreen(
    viewModel: AdminDashboardViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Button(onClick = onBackClick) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Manage Cars", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        viewModel.carCategories.forEach { (label, imageUrl) ->
            CarImageCardReadOnly(
                label = label,
                imageUrl = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
fun CarImageCardReadOnly(label: String, imageUrl: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Image: $imageUrl", style = MaterialTheme.typography.bodySmall)
        }
    }
}
