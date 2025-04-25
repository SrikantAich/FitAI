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
import com.example.finalproject.getStepCount
import com.example.finalproject.getUserProfile
import com.example.finalproject.saveDistanceAndCalories

@Composable
fun CaloriesDetailsScreen() {
    val context = LocalContext.current
    val steps = getStepCount(context) // Get steps from SharedPreferences
    val userProfile = getUserProfile(context) // Fetch weight and height from SharedPreferences

    val weight = userProfile.weight // Use the fetched weight
    val height = userProfile.height // Use the height for future use (e.g., BMI)

    // Check if weight or height is invalid (0f indicates missing data)
    if (weight == 0f || height == 0f) {
        Text(
            text = "Invalid profile data. Please update your profile.",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.error
        )
        return // Exit early if data is invalid
    }

    // Calculate distance per step (in meters)
    val stepLength = 0.762 // Average step length in meters
    val distanceCovered = steps * stepLength // Total distance covered in meters

    // Calories burned per kilogram per meter (rough estimate)
    val caloriesPerKgPerMeter = 0.04

    // Calculate total calories burned
    val caloriesBurned = (distanceCovered * weight * caloriesPerKgPerMeter).toInt()

    // Save the distance and calories in SharedPreferences
    saveDistanceAndCalories(context, distanceCovered.toFloat(), caloriesBurned)

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Calories Burned Details",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp).padding(top =60.dp)
        )

        // Stat Card for Steps
        StatCard(title = "Total Steps", value = "$steps steps", emoji = "👣") {
            // Handle card click (for example, navigate to a detailed step screen)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stat Card for Calories Burned
        StatCard(
            title = "Calories Burned",
            value = "$caloriesBurned kcal",
            emoji = "🔥",
            onClick = {
                // Handle card click (e.g., show more details or navigate)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stat Card for Distance Covered
        StatCard(
            title = "Distance Covered",
            value = "${String.format("%.2f", distanceCovered)} meters",
            emoji = "🏃‍♂️",
            onClick = {
                // Handle card click (for example, navigate to a detailed distance screen)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
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

@Preview(showBackground = true)
@Composable
fun PreviewCaloriesDetailsScreen() {
    CaloriesDetailsScreen()
}
