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
import androidx.compose.runtime.LaunchedEffect

/**
 * Navigation function sets up the navigation graph for the application.
 * Defines the navigation routes and their associated composable screens.
 *
 * @param navController The NavHostController used for navigation between screens.
 */
@Composable
fun Navigation(navController: NavHostController) {
    // Define the navigation graph and its routes
    NavHost(navController = navController, startDestination = "auth") {

        // Route: Authentication Screen
        composable("auth") {
            AuthScreen(
                onLoginSuccess = { email ->
                    // Navigate to Main Screen upon successful login
                    navController.navigate("main/$email") {
                        popUpTo("auth") { inclusive = true } // Remove "auth" from the back stack
                    }
                },
                onProfileInput = { email ->
                    // Navigate to Profile Input Screen if user profile is incomplete
                    navController.navigate("profileInput/$email") {
                        popUpTo("auth") { inclusive = true } // Remove "auth" from the back stack
                    }
                },
                navController = navController
            )
        }

        // Route: Main Screen
        composable("main/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                // Load the Main Screen with the provided email
                MainScreen(email = email)
            }
        }

        // Route: Profile Input Screen
        composable("profileInput/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                // Load the Profile Input Screen with the provided email
                ProfileInputScreen(email = email) {
                    // Navigate to Main Screen after completing profile input
                    navController.navigate("main/$email") {
                        popUpTo("auth") { inclusive = true } // Remove "auth" from the back stack
                    }
                }
            }
        }
    }
}

/**
 * Function to check if a user's profile is complete.
 * Verifies if both the height and weight fields are populated.
 *
 * @param email The user's email address, used to retrieve their profile.
 * @param db The UserDao instance for accessing the database.
 * @return Boolean indicating whether the profile is complete.
 */
suspend fun isProfileComplete(email: String, db: UserDao): Boolean {
    // Retrieve user information from the database on an IO thread
    val user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
    // Profile is complete if both height and weight are not null
    return user?.height != null && user.weight != null
}




