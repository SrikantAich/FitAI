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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.FitnessViewModel
import com.example.finalproject.UiState

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
    var isChecked by remember { mutableStateOf(false) }

    if (!isChecked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }, // Update state when clicked
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = exercise,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
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
