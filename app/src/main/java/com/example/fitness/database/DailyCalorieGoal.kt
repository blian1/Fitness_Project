package com.example.fitness.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "daily_calorie_goals")
data class DailyCalorieGoal(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id") @set:PropertyName("id")
    var id: Int = 0, // Local primary key for Room

    @get:PropertyName("email") @set:PropertyName("email")
    var email: String, // User's email address

    @get:PropertyName("date") @set:PropertyName("date")
    var date: String, // Date of the calorie goal

    @get:PropertyName("intake") @set:PropertyName("intake")
    var intake: Float, // Recommended daily calorie intake

    @get:PropertyName("burn") @set:PropertyName("burn")
    var burn: Float // Recommended daily calorie burned
)
