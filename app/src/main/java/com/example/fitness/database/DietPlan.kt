package com.example.fitness.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_plans")
data class DietPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val date: String, // Date
    val type: String, // MealType(Lunch,dinner...)
    val content: String, // MealContent
    val calories: Double // calories for this meal
)
