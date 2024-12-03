package com.example.fitness.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitness.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A composable screen for user profile input. It collects the user's personal details
 * such as name, age, height, weight, and fitness goal, and saves the data to the database.
 *
 * @param email The email of the user whose profile is being updated.
 * @param onProfileSaved A callback function to navigate to the next screen after the profile is saved successfully.
 */
@Composable
fun ProfileInputScreen(email: String, onProfileSaved: () -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    // User inputs
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var goal by remember { mutableStateOf("gain muscle") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input fields for user profile
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (m)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for selecting goal
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { expanded = true }) {
                Text("Select Goal: $goal")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Gain Muscle") },
                    onClick = {
                        goal = "gain muscle"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Lose Weight") },
                    onClick = {
                        goal = "lose weight"
                        expanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                scope.launch {
                    val userAge = age.toIntOrNull()
                    val userHeight = height.toFloatOrNull()
                    val userWeight = weight.toFloatOrNull()

                    if (name.isBlank() || userAge == null || userHeight == null || userWeight == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Calculate BMI
                        val bmi = userWeight / (userHeight * userHeight)

                        // Call the updateUserProfile method
                        val rowsUpdated = withContext(Dispatchers.IO) {
                            db.updateUserProfile(
                                email = email,
                                name = name,
                                age = userAge,
                                height = userHeight,
                                weight = userWeight,
                                goal = goal,
                                bmi = bmi
                            )
                        }

                        withContext(Dispatchers.Main) {
                            if (rowsUpdated > 0) {
                                Toast.makeText(context, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                                onProfileSaved() // Trigger navigation to the next screen
                            } else {
                                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}


