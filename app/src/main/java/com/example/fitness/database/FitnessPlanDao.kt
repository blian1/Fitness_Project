package com.example.fitness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FitnessPlanDao {
    @Insert
    fun insertFitnessPlan(vararg fitnessPlans: FitnessPlan): List<Long>

    @Query("SELECT * FROM fitness_plans WHERE email = :email AND date = :date")
    fun getFitnessPlansByDate(email: String, date: String): List<FitnessPlan>

    @Query("DELETE FROM fitness_plans WHERE email = :email AND date = :date")
    fun deleteFitnessPlansByDate(email: String, date: String): Int
}

