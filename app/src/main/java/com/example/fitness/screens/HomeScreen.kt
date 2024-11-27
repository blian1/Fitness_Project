package com.example.fitness.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitness.database.AppDatabase
import com.example.fitness.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(email: String) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    // State to hold the user data
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Load user data from database
    LaunchedEffect(email) {
        scope.launch {
            user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
            isLoading = false
        }
    }

    // Display content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Text(text = "Loading...", style = MaterialTheme.typography.headlineMedium)
        } else if (user != null) {
            Text(
                text = "Welcome! ${user?.name ?: "Guest"}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Weight: ${user?.weight ?: "N/A"} KG",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Height: ${user?.height ?: "N/A"} m",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "BMI : ${user?.bmi?.toString() ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Age: ${user?.age ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Goal: ${user?.goal ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Today Plan", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(256.dp))

            Text(text = "Today Diet", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(text = "User data not found.", style = MaterialTheme.typography.headlineMedium)
        }
    }
}



