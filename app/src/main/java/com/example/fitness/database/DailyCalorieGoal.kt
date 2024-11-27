package com.example.fitness.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_calorie_goals")
data class DailyCalorieGoal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val date: String, // Date
    val intake: Float, // Everyday recommend intake
    val burn: Float // Everyday recommend burned
)
