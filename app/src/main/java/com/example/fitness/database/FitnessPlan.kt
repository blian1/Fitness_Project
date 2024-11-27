package com.example.fitness.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness_plans")
data class FitnessPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val date: String, // Date
    val type: String, // Fitness type
    val content: String, // Content
    val calories: Double // Calories burn
)
