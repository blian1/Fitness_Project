package com.example.fitness.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitness.components.BottomNavBar
import com.example.fitness.screens.*

/**
 * MainScreen composable function that serves as the entry point for the app's navigation.
 * It includes a Scaffold layout with a bottom navigation bar and handles navigation between screens.
 *
 * @param email The email of the currently logged-in user, passed to relevant screens as needed.
 */
@Composable
fun MainScreen(email: String) {
    // Create a NavController to manage app navigation
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(email = email, navController = navController)
            }
            composable(Screen.Fitness.route) { FitnessScreen() }
            composable(Screen.Diet.route) { DietScreen() }
            composable(Screen.AIHelper.route) { AIScreen() }
            composable(Screen.Calendar.route) { CalendarScreen(email = email) }
        }
    }
}

