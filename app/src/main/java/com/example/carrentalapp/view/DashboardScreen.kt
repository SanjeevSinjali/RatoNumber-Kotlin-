package com.example.carrentalapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.carrentalapp.viewmodel.DashboardViewModel
import com.example.carrentalapp.view.LoginActivity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                viewModel.navigationItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item) },
                        selected = viewModel.selectedMenuItem.value == item,
                        onClick = {
                            scope.launch { drawerState.close() }

                            if (item == "Sign Out") {
                                context.startActivity(Intent(context, LoginActivity::class.java))
                            } else {
                                viewModel.selectedMenuItem.value = item
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("रातोNumber", color = Color.Red) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu Icon")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                when (viewModel.selectedMenuItem.value) {
                    "Home" -> CarBookingForm(viewModel)
                    "Cars" -> CarSectionScrollable(viewModel)
                    "Profile" -> UpdateProfileScreen(viewModel)  // Added Profile screen here
                    "Settings" -> Text("Settings screen (To be implemented)", modifier = Modifier.padding(16.dp))
                    else -> Text("Unknown screen", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun CarBookingForm(viewModel: DashboardViewModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.location.value,
            onValueChange = { viewModel.location.value = it },
            placeholder = { Text("Pick-up and return location") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedCardItemEditable("Pick-up date", viewModel.pickupDate.value, { viewModel.pickupDate.value = it }, Modifier.weight(1f))
            OutlinedCardItemEditable("Time", viewModel.pickupTime.value, { viewModel.pickupTime.value = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedCardItemEditable("Return date", viewModel.returnDate.value, { viewModel.returnDate.value = it }, Modifier.weight(1f))
            OutlinedCardItemEditable("Time", viewModel.returnTime.value, { viewModel.returnTime.value = it }, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Available Cars", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        viewModel.carCategories.forEach { (label, imageUrl) ->
            CarImageCard(label = label, imageUrl = imageUrl, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(64.dp)) // bottom padding
    }
}

@Composable
fun CarSectionScrollable(viewModel: DashboardViewModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Available Cars", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        viewModel.carCategories.forEach { (label, imageUrl) ->
            CarImageCard(
                label = label,
                imageUrl = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun OutlinedCardItemEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    OutlinedCard(modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(it, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(label, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun CarImageCard(label: String, imageUrl: String, modifier: Modifier = Modifier) {
    val painter = if (imageUrl.startsWith("http")) {
        rememberAsyncImagePainter(imageUrl)
    } else {
        val context = LocalContext.current
        val resId = remember(imageUrl) {
            context.resources.getIdentifier(
                imageUrl.substringBeforeLast('.'),
                "drawable",
                context.packageName
            )
        }
        painterResource(id = resId)
    }

    Card(
        modifier = modifier
            .height(200.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painter,
                contentDescription = label,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}