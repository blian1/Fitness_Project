package com.example.fitness.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "diet_plans")
data class DietPlan(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("id") @set:PropertyName("id")
    var id: Int = 0, // Local primary key for Room

    @get:PropertyName("email") @set:PropertyName("email")
    var email: String, // User's email address

    @get:PropertyName("date") @set:PropertyName("date")
    var date: String, // Date of the meal plan

    @get:PropertyName("type") @set:PropertyName("type")
    var type: String, // Meal type (e.g., Breakfast, Lunch)

    @get:PropertyName("content") @set:PropertyName("content")
    var content: String, // Description of the meal content

    @get:PropertyName("calories") @set:PropertyName("calories")
    var calories: Double // Calories in this meal
)

