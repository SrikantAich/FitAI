package com.example.finalproject



import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController




import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(navController: NavHostController) {
    var userId by remember { mutableStateOf("") } // Email
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // User ID input (Email)
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up button
        Button(onClick = {
            if (userId.isNotBlank() && password.isNotBlank()) {
                // Firebase Authentication - create user with email and password
                auth.createUserWithEmailAndPassword(userId, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign up successful
                            Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to the SignUpDetailScreen to fill in additional details
                            navController.navigate("signUpDetailScreen")
                        } else {
                            // Show error message if signup fails
                            Toast.makeText(
                                context,
                                "Signup failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Sign Up")
        }
    }
}
