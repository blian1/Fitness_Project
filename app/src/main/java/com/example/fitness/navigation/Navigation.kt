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


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onLoginSuccess = { email ->
                    navController.navigate("main/$email") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onProfileInput = { email ->
                    navController.navigate("profileInput/$email") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                navController = navController
            )
        }
        composable("main/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                MainScreen(email = email)
            }
        }
        composable("profileInput/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (!email.isNullOrEmpty()) {
                ProfileInputScreen(email = email) {
                    navController.navigate("main/$email") {
                        popUpTo("auth") { inclusive = true }
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




