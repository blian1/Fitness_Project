package com.example.fitness.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitness.database.AppDatabase
import com.example.fitness.database.UserDao
import com.example.fitness.screens.AuthScreen
import com.example.fitness.screens.HomeScreen
import com.example.fitness.screens.MainScreen
import com.example.fitness.screens.ProfileInputScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context).userDao()

            AuthScreen(
                onLoginSuccess = { email ->
                    scope.launch {
                        val emailString = email as? String
                        if (emailString != null) {
                            // Check if the profile is complete
                            if (isProfileComplete(emailString, db)) {
                                // Navigate to HomeScreen
                                navController.navigate("home/$emailString") {
                                    popUpTo("auth") { inclusive = true } // Clear stack
                                }
                            } else {
                                // Navigate to ProfileInputScreen
                                navController.navigate("profileInput/$emailString")
                            }
                        }
                    }
                },
                navController = navController
            )
        }

        // HomeScreen with user's email
        composable("home/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                MainScreen(email) // Pass email to MainScreen
            }
        }

        // ProfileInputScreen for entering user details
        composable("profileInput/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                ProfileInputScreen(email) {
                    // Navigate to HomeScreen after saving profile
                    navController.navigate("home/$email") {
                        popUpTo("auth") { inclusive = true } // Clear stack
                    }
                }
            }
        }
    }
}

/**
 * Checks if the user's profile is complete.
 * Only checks height and weight for simplicity.
 */
suspend fun isProfileComplete(email: String, db: UserDao): Boolean {
    val user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
    // Profile is considered complete if height and weight are not null
    return user?.height != null && user.weight != null
}


