package com.example.fitness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DietPlanDao {
    @Insert
    fun insertDietPlan(vararg dietPlans: DietPlan): List<Long>

    @Query("SELECT * FROM diet_plans WHERE email = :email AND date = :date")
    fun getDietPlansByDate(email: String, date: String): List<DietPlan>

    @Query("DELETE FROM diet_plans WHERE email = :email AND date = :date")
    fun deleteDietPlansByDate(email: String, date: String): Int
}

