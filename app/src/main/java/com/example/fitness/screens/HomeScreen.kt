package com.example.fitness.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitness.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * The HomeScreen composable displays the user's basic information and today's fitness and diet plans.
 *
 * @param email The email of the logged-in user to fetch their data from the database.
 * @param navController The navigation controller to handle screen transitions.
 */
@Composable
fun HomeScreen(email: String, navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    // Get today's date
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    // State for user and plan data
    var user by remember { mutableStateOf<User?>(null) }
    var calorieGoal by remember { mutableStateOf<DailyCalorieGoal?>(null) }
    var fitnessPlans by remember { mutableStateOf<List<FitnessPlan>>(emptyList()) }
    var dietPlans by remember { mutableStateOf<List<DietPlan>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load user and today's plan from database
    LaunchedEffect(email) {
        scope.launch {
            try {
                user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                calorieGoal = withContext(Dispatchers.IO) { db.dailyCalorieGoalDao().getDailyCalorieGoalByDate(email, today) }
                fitnessPlans = withContext(Dispatchers.IO) { db.fitnessPlanDao().getFitnessPlansByDate(email, today) }
                dietPlans = withContext(Dispatchers.IO) { db.dietPlanDao().getDietPlansByDate(email, today) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            item { Text(text = "Loading...", style = MaterialTheme.typography.headlineMedium) }
        } else if (user != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Welcome! ${user?.name ?: "Guest"}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Button(onClick = {
                        navController.navigate("auth") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }) {
                        Text(text = "Logout")
                    }
                }

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
            }

            if (calorieGoal != null && fitnessPlans.isNotEmpty() && dietPlans.isNotEmpty()) {
                item {
                    Text(text = "Today's Plan", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Daily Calorie Goal:")
                    Text(text = "Intake: ${calorieGoal!!.intake} kcal")
                    Text(text = "Burn: ${calorieGoal!!.burn} kcal")
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Fitness Plan:")
                }

                // Show fitness plans
                items(fitnessPlans) { plan ->
                    Text(text = "- ${plan.type}: ${plan.content} (${plan.calories} kcal)")
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Diet Plan:")
                }

                // Show diet plans
                items(dietPlans) { plan ->
                    Text(text = "- ${plan.type}: ${plan.content} (${plan.calories} kcal)")
                }
            } else {
                item {
                    Text(
                        text = "No plan for today. Go to generate a plan.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            item { Text(text = "User data not found.", style = MaterialTheme.typography.headlineMedium) }
        }
    }
}




