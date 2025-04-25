package com.example.finalproject

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ProfilePage() {
    val context = LocalContext.current

    // Retrieve user profile details from PreferenceManager
    val userProfile = getUserProfile(context)


    // State variables to hold the editable values
    var name by remember { mutableStateOf(TextFieldValue(userProfile.name)) }
    var age by remember { mutableStateOf(TextFieldValue(userProfile.age.toString())) }
    var height by remember { mutableStateOf(TextFieldValue(userProfile.height.toString())) }
    var weight by remember { mutableStateOf(TextFieldValue(userProfile.weight.toString())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Edit Your Profile",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 32.dp).padding(top = 60.dp)
        )

        // Editable Name
        EditableStatCard(
            title = "Name",
            value = name,
            emoji = "👤",
            onValueChange = { name = it }
        )

        // Editable Age
        EditableStatCard(
            title = "Age",
            value = age,
            emoji = "🎂",
            onValueChange = { age = it }
        )

        // Editable Height
        EditableStatCard(
            title = "Height",
            value = height,
            emoji = "📏",
            onValueChange = { height = it }
        )

        // Editable Weight
        EditableStatCard(
            title = "Weight",
            value = weight,
            emoji = "⚖️",
            onValueChange = { weight = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button to save changes
        Button(
            onClick = {
                // Save the updated profile data using PreferenceManager
                val saveProfile=saveUserProfile(
                    context,
                    name.text,
                    age.text.toInt(),
                    height.text.toFloat(),
                    weight.text.toFloat()
                )
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp), // Add padding for better alignment
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)

        }
    }
}

@Composable
fun EditableStatCard(title: String, value: TextFieldValue, emoji: String, onValueChange: (TextFieldValue) -> Unit) {
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
