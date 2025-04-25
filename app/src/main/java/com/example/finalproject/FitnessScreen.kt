package com.example.finalproject

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun FitnessApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "fitnessScreen") {
        composable("fitnessScreen") {
            FitnessScreen(navController = navController) // Pass the navController
        }
        composable("stepsDetails") {
            StepsDetailsScreen() // Navigate to Steps Details Screen
        }
    }
}

@Composable
fun StatCard(title: String, value: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ensure the card takes the full width
            .padding(8.dp) // Apply consistent padding around the card
            .clickable { onClick() }, // Make the card clickable
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp), // Padding inside the card
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp) // Padding between emoji and value
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp) // Padding between value and title
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FitnessScreen(navController: NavController) {
    val context = LocalContext.current
    var steps by remember { mutableStateOf(0) }
    var calories by remember { mutableStateOf(0) }
    val userProfile = getUserProfile(context)
    // Fetch steps from SharedPreferences
    LaunchedEffect(Unit) {
        steps = getStepCount(context)
         // Get saved calories from SharedPreferences
    }
    calories = getSavedCalories(context)

    val bodyParts = listOf("Full Body", "Cardio", "Chest", "Back", "Legs", "Arms", "Core")
    val durations = listOf("10 mins", "15 mins", "20 mins", "30 mins", "45 mins", "60 mins")

    var selectedBodyPart by rememberSaveable { mutableStateOf(bodyParts.first()) }
    var selectedDuration by rememberSaveable { mutableStateOf(durations[2]) }

    val fitnessViewModel: FitnessViewModel = viewModel()
    val uiState by fitnessViewModel.uiState.collectAsState()

    // Here you can replace "Username" with the actual username from SharedPreferences or a ViewModel
    val username = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Custom Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Welcome, ${userProfile.name}",
                )
            },
            modifier = Modifier.fillMaxWidth(),

            actions = {
                // You can add actions here like a settings button or a profile button
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fitness Planner",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 24.dp)
                .padding(top = 10.dp)
        )
        StatCard(
            title = "Profile",
            value = "View your profile",
            emoji = "👤",
            onClick = { navController.navigate("profilePage") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        StatCard(
            title = "Calories Burned",
            value = "$calories kcal",
            emoji = "🔥",
            onClick = { navController.navigate("caloriesDetails") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        StatCard(
            title = "Steps",
            value = "$steps",
            emoji = "🏃‍",
            onClick = { navController.navigate("stepsDetails") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        StatCard(
            title = "Streak",
            value = "7 Days",
            emoji = "🏅",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(16.dp))

        StatCard(
            title = "Workout Plan",
            value = "AI Workout Planner",
            emoji = "💪",
            onClick = {navController.navigate("AIWorkoutPlanner") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                val response = (uiState as UiState.Success).outputText
                val lines = response.split("\n", limit = 2)
                val heading = lines.getOrNull(0) ?: ""
                val subheading = lines.getOrNull(1) ?: ""

                Text(
                    text = heading,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(text = subheading, style = MaterialTheme.typography.bodyLarge)
            }

            is UiState.Error -> Text((uiState as UiState.Error).errorMessage)
            else -> {}
        }


    }
}



// Steps Details Screen
@Composable
fun StepsDetailsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Steps History and Details", style = MaterialTheme.typography.headlineMedium)

        Text(text = "Total Steps: 600", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Goal: 10000 steps", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
@Preview
fun show() {
    FitnessScreen(navController = rememberNavController())
}
