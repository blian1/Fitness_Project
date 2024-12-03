package com.example.fitness.repository

import android.util.Log
import com.example.fitness.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FitnessRepository(
    private val userDao: UserDao,
    private val fitnessPlanDao: FitnessPlanDao,
    private val dietPlanDao: DietPlanDao,
    private val dailyCalorieGoalDao: DailyCalorieGoalDao,
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FITNESS_PLANS_COLLECTION = "fitness_plans"
        private const val DIET_PLANS_COLLECTION = "diet_plans"
        private const val DAILY_CALORIE_GOALS_COLLECTION = "daily_calorie_goals"
    }

    suspend fun syncUserData(email: String) {
        val user = userDao.getUserByEmail(email)
        if (user != null) {
            try {
                firestore.collection(USERS_COLLECTION).document(email).set(user).await()
                Log.d("FirestoreSync", "User synced to Firestore: $email")
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync user data to Firestore: ${e.message}")
            }
        }
    }

    suspend fun syncAllData() {
        try {
            // Sync users
            val users = userDao.getAllUsers()
            for (user in users) {
                firestore.collection(USERS_COLLECTION).document(user.email).set(user).await()
            }

            // Sync fitness plans
            val fitnessPlans = fitnessPlanDao.getAllFitnessPlans()
            for (plan in fitnessPlans) {
                firestore.collection(FITNESS_PLANS_COLLECTION).document("${plan.email}_${plan.date}").set(plan).await()
            }

            // Sync diet plans
            val dietPlans = dietPlanDao.getAllDietPlans()
            for (plan in dietPlans) {
                firestore.collection(DIET_PLANS_COLLECTION).document("${plan.email}_${plan.date}").set(plan).await()
            }

            // Sync daily calorie goals
            val dailyCalorieGoals = dailyCalorieGoalDao.getAllDailyCalorieGoals()
            for (goal in dailyCalorieGoals) {
                firestore.collection(DAILY_CALORIE_GOALS_COLLECTION).document("${goal.email}_${goal.date}").set(goal).await()
            }
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Failed to sync all data: ${e.message}")
        }
    }


    private suspend fun <T : Any> syncCollection(localData: List<T>, collectionName: String) {
        for (data in localData) {
            try {
                firestore.collection(collectionName).add(data).await()
                Log.d("FirestoreSync", "$collectionName synced successfully")
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync $collectionName: ${e.message}")
            }
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email) ?: try {
            val snapshot = firestore.collection(USERS_COLLECTION).document(email).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error fetching user from Firestore: ${e.message}")
            null
        }
    }

    suspend fun updateUserProfile(user: User) {
        userDao.updateUserProfile(user.email, user.name!!, user.age!!, user.height!!, user.weight!!, user.goal!!, user.bmi!!)
        try {
            firestore.collection(USERS_COLLECTION).document(user.email).set(user).await()
            Log.d("FirestoreSync", "User profile updated in Firestore: ${user.email}")
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Failed to update user profile in Firestore: ${e.message}")
        }
    }
}
