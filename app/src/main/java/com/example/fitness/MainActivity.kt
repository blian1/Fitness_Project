package com.example.fitness

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.fitness.navigation.Navigation
import com.example.fitness.repository.FitnessRepository
import com.example.fitness.database.AppDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Get Firestore and local database instances
        val firestore = FirebaseFirestore.getInstance()
        val database = AppDatabase.getDatabase(this)

        // Initialize repository
        val repository = FitnessRepository(
            userDao = database.userDao(),
            fitnessPlanDao = database.fitnessPlanDao(),
            dietPlanDao = database.dietPlanDao(),
            dailyCalorieGoalDao = database.dailyCalorieGoalDao(),
            firestore = firestore
        )
        Log.d("MainActivity", "Repository initialized: $repository")

        // Sync data on app start
        lifecycleScope.launch {
            syncData(repository)
        }

        // Set up Compose UI
        setContent {
            val navController = rememberNavController()
            Navigation(navController)
        }
    }

    private suspend fun syncData(repository: FitnessRepository) {
        withContext(Dispatchers.IO) {
            try {
                repository.syncAllData()
                Log.d("MainActivity", "Data synchronization completed successfully.")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during data synchronization: ${e.message}")
            }
        }
    }
}
