import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.finalproject.EditableStatCard
import com.example.finalproject.FitnessViewModel
import com.example.finalproject.getSavedGoal
import com.example.finalproject.getStepCount
import com.example.finalproject.saveGoal
import com.example.finalproject.saveStepCount
import kotlin.math.pow
import kotlin.math.sqrt
@Composable
fun StepsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    // Load saved step count and goal from SharedPreferences
    var steps by remember { mutableStateOf(getStepCount(context)) }
    var goal by remember { mutableStateOf(getSavedGoal(context)) }

    val stepThreshold = 12f
    val lastValues = remember { mutableStateOf(Triple(0f, 0f, 0f)) }

    // Registering the SensorEventListener
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val (x, y, z) = Triple(it.values[0], it.values[1], it.values[2])
                    val prev = lastValues.value
                    val delta = sqrt(
                        (x - prev.first).toDouble().pow(2.0) +
                                (y - prev.second).toDouble().pow(2.0) +
                                (z - prev.third).toDouble().pow(2.0)
                    ).toFloat()

                    if (delta > stepThreshold) {
                        steps += 1
                        saveStepCount(context, steps) // Save steps to SharedPreferences
                    }

                    lastValues.value = Triple(x, y, z)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // UI that displays steps, goal, and allows setting a new goal
    StepsUI(
        steps = steps,
        goal = goal,
        setGoal = { newGoal ->
            goal = newGoal
            saveGoal(context, newGoal) // Save new goal to SharedPreferences
        },
        navController = navController
    )
}

@Composable
fun StepsUI(steps: Int, goal: Int, setGoal: (Int) -> Unit, navController: NavHostController) {
    val percentage = (steps.toFloat() / goal).coerceIn(0f, 1f)
    val goalText = remember { mutableStateOf(goal.toString()) } // To track the editable goal value

    // Determine the color based on the percentage
    val progressColor = when {
        percentage < 0.4f -> Color.Red // Red
        percentage < 0.9f -> Color.Yellow // Yellow
        else -> Color.Green // Green
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("🏃‍♂️ Steps Tracker", fontSize = 28.sp, style = MaterialTheme.typography.headlineMedium)

        // Circular Progress Indicator (showing the steps progress)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp)
        ) {
            CircularProgressIndicator(
                progress = percentage,
                strokeWidth = 12.dp,
                color = progressColor,
                modifier = Modifier.fillMaxSize()
            )

            // Show steps, progress, and remaining steps inside the ring
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("$steps", fontSize = 40.sp, style = MaterialTheme.typography.headlineLarge)
                Text("${(percentage * 100).toInt()}%", fontSize = 16.sp)
                Text("${(goal - steps).coerceAtLeast(0)} steps remaining", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Stat Card for setting the goal
        EditableStatCard(
            title = "Set Your Daily Goal",
            value = goalText.value,
            emoji = "🎯",
            onValueChange = { newGoal ->
                goalText.value = newGoal // Update goal value
                val newGoalInt = newGoal.toIntOrNull()
                if (newGoalInt != null) {
                    setGoal(newGoalInt) // Update goal in SharedPreferences
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation button to go back
        Button(onClick = { navController.popBackStack() }) {
            Text("🔙 Back")
        }
    }
}
@Composable
fun EditableStatCard(title: String, value: String, emoji: String, onValueChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ensure the card takes the full width
            .padding(8.dp), // Apply consistent padding around the card
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

            // Editable Text Field for the value
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(title) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}