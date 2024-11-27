package com.example.fitness.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fitness.database.AppDatabase
import com.example.fitness.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitness.navigation.isProfileComplete


/**
 * Authentication screen for login and registration.
 * This Composable handles user authentication logic, including login and registration forms.
 *
 * @param onLoginSuccess Callback invoked when the user successfully logs in.
 */
@Composable
fun AuthScreen(onLoginSuccess: (String) -> Unit, navController: NavHostController) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Login" else "Register",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Submit button for Login or Register
        Button(
            onClick = {
                scope.launch {
                    try {
                        if (isLogin) {
                            // Login logic
                            val user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
                            if (user != null && user.password == password) {
                                // Check profile completeness
                                val isComplete = withContext(Dispatchers.IO) {
                                    isProfileComplete(email, db)
                                }
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                    if (isComplete) {
                                        // Navigate to HomeScreen if profile is complete
                                        navController.navigate("home/$email") {
                                            popUpTo("auth") { inclusive = true } // Clear back stack
                                        }
                                    } else {
                                        // Navigate to ProfileInputScreen if profile is incomplete
                                        navController.navigate("profileInput/$email")
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // Register logic
                            if (email.isBlank() || password.isBlank()) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                withContext(Dispatchers.IO) {
                                    db.insertUser(User(email = email, password = password))
                                }
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                                }
                                isLogin = true
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Login" else "Register")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between Login and Register
        TextButton(onClick = { isLogin = !isLogin }) {
            Text(if (isLogin) "Don't have an account? Register" else "Already have an account? Login")
        }
    }
}
