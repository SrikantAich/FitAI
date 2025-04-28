import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.FitnessViewModel
import com.example.finalproject.UiState
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite

@Composable
fun AIWorkoutPlannerScreen() {
    val bodyParts = listOf("Full Body", "Cardio", "Chest", "Back", "Legs", "Arms", "Core")
    val durations = listOf("10 mins", "15 mins", "20 mins", "30 mins", "45 mins", "60 mins")

    var selectedBodyPart by remember { mutableStateOf(bodyParts.first()) }
    var selectedDuration by remember { mutableStateOf(durations[2]) }

    val fitnessViewModel: FitnessViewModel = viewModel()
    val uiState by fitnessViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AI Workout Planner",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 60.dp, bottom = 24.dp)
        )

        BodyPartDropdown(bodyParts, selectedBodyPart) { selectedBodyPart = it }
        Spacer(modifier = Modifier.height(16.dp))

        DurationDropdown(durations, selectedDuration) { selectedDuration = it }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val prompt = "Give only heading and subheading. Heading should be '${selectedBodyPart.uppercase()} Plan'. Subheading should contain list of exercises for ${selectedDuration.lowercase()}."
                fitnessViewModel.sendPrompt(null, prompt)
            }
        ) {
            Text("Generate Plan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator()

            is UiState.Success -> {
                val response = (uiState as UiState.Success).outputText
                val lines = response.split("\n", limit = 2)
                val heading = lines.getOrNull(0) ?: ""
                val subheading = lines.getOrNull(1) ?: ""

                val initialExercises = subheading
                    .split(",", "\n", "•", "·")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                val exerciseList = remember { mutableStateListOf(*initialExercises.toTypedArray()) }

                Text(
                    text = heading,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                exerciseList.forEach { exercise ->
                    StatCard(exercise)
                }
            }

            is UiState.Error -> Text(
                text = (uiState as UiState.Error).errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            else -> {}
        }
    }
}

@Composable
fun StatCard(exercise: String) {
    var isClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Default background color, changing when clicked
    val cardBackgroundColor = if (isClicked) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    // Card layout for the exercise
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .clickable {
                // Show a toast message when clicked
//                Toast.makeText(context, "Workout Completed", Toast.LENGTH_SHORT).show()
                isClicked = !isClicked  // Toggle completion state
            },
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Default icon for workout-related activity
            Icon(
                imageVector = Icons.Default.ArrowForward,  // Change to a valid icon
                contentDescription = "Favorite Icon",
                modifier = Modifier.size(36.dp).padding(end = 16.dp),
                tint = if (isClicked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )

            // Exercise name with conditional styling
            Text(
                text = exercise,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isClicked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isClicked) TextDecoration.LineThrough else TextDecoration.None,
                    fontWeight = if (isClicked) FontWeight.Light else FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )

            // Checkmark icon when the workout is completed
            if (isClicked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun BodyPartDropdown(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Select Body Part",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DurationDropdown(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Select Duration",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
