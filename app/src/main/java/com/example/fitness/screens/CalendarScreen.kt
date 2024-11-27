package com.example.fitness.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitness.database.*
import com.example.fitness.utils.generateDailyPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(email: String) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    // Get today's date
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    // State for user data
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isGenerating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State for plan data
    var calorieGoal by remember { mutableStateOf<DailyCalorieGoal?>(null) }
    var fitnessPlans by remember { mutableStateOf<List<FitnessPlan>>(emptyList()) }
    var dietPlans by remember { mutableStateOf<List<DietPlan>>(emptyList()) }

    // Load user and today's plan
    LaunchedEffect(email) {
        scope.launch {
            isLoading = true
            try {
                user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                calorieGoal = withContext(Dispatchers.IO) { db.dailyCalorieGoalDao().getDailyCalorieGoalByDate(email, today) }
                fitnessPlans = withContext(Dispatchers.IO) { db.fitnessPlanDao().getFitnessPlansByDate(email, today) }
                dietPlans = withContext(Dispatchers.IO) { db.dietPlanDao().getDietPlansByDate(email, today) }
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Text(text = "Loading user data...")
        } else if (isGenerating) {
            Text(text = "Generating your daily plan, please wait...")
        } else if (!errorMessage.isNullOrEmpty()) {
            Text(text = "Error: $errorMessage")
        } else if (user != null) {
            if (calorieGoal != null) {
                // Show existing plan
                Text(text = "Today's Plan:")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Daily Calorie Goal:")
                Text(text = "Intake: ${calorieGoal!!.intake} kcal")
                Text(text = "Burn: ${calorieGoal!!.burn} kcal")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Fitness Plan:")
                LazyColumn {
                    items(fitnessPlans) { plan ->
                        Text(text = "- ${plan.type}: ${plan.content} (${plan.calories} kcal)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Diet Plan:")
                LazyColumn {
                    items(dietPlans) { plan ->
                        Text(text = "- ${plan.type}: ${plan.content} (${plan.calories} kcal)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to regenerate plan
                Button(onClick = {
                    isGenerating = true
                    errorMessage = null
                    scope.launch {
                        try {
                            // Delete old plans
                            withContext(Dispatchers.IO) {
                                db.dailyCalorieGoalDao().deleteDailyCalorieGoalByDate(email, today)
                                db.fitnessPlanDao().deleteFitnessPlansByDate(email, today)
                                db.dietPlanDao().deleteDietPlansByDate(email, today)
                            }

                            // Generate new plan
                            val (calGoal, fitPlans, dietPlansResult) = generateDailyPlan(
                                email = user!!.email,
                                age = user!!.age ?: 0,
                                weight = user!!.weight ?: 0f,
                                height = user!!.height ?: 0f,
                                goal = user!!.goal ?: "general fitness"
                            )

                            // Save new plan to database
                            withContext(Dispatchers.IO) {
                                db.dailyCalorieGoalDao().insertDailyCalorieGoal(calGoal)
                                db.fitnessPlanDao().insertFitnessPlan(*fitPlans.toTypedArray())
                                db.dietPlanDao().insertDietPlan(*dietPlansResult.toTypedArray())
                            }

                            // Update UI
                            calorieGoal = calGoal
                            fitnessPlans = fitPlans
                            dietPlans = dietPlansResult
                        } catch (e: Exception) {
                            errorMessage = "Failed to regenerate plan: ${e.message}"
                        } finally {
                            isGenerating = false
                        }
                    }
                }) {
                    Text(text = "Don't like this plan? Regenerate")
                }
            } else {
                // Generate new plan if none exists
                Text(text = "No plan for today. Generate one!")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    isGenerating = true
                    errorMessage = null
                    scope.launch {
                        try {
                            // Generate new plan
                            val (calGoal, fitPlans, dietPlansResult) = generateDailyPlan(
                                email = user!!.email,
                                age = user!!.age ?: 0,
                                weight = user!!.weight ?: 0f,
                                height = user!!.height ?: 0f,
                                goal = user!!.goal ?: "general fitness"
                            )

                            // Save new plan to database
                            withContext(Dispatchers.IO) {
                                db.dailyCalorieGoalDao().insertDailyCalorieGoal(calGoal)
                                db.fitnessPlanDao().insertFitnessPlan(*fitPlans.toTypedArray())
                                db.dietPlanDao().insertDietPlan(*dietPlansResult.toTypedArray())
                            }

                            // Update UI
                            calorieGoal = calGoal
                            fitnessPlans = fitPlans
                            dietPlans = dietPlansResult
                        } catch (e: Exception) {
                            errorMessage = "Failed to generate plan: ${e.message}"
                        } finally {
                            isGenerating = false
                        }
                    }
                }) {
                    Text(text = "Generate Plan")
                }
            }
        } else {
            Text(text = "User data not found.")
        }
    }
}
