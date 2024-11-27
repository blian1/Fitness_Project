package com.example.fitness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailyCalorieGoalDao {
    @Insert
    fun insertDailyCalorieGoal(goal: DailyCalorieGoal): Long

    @Query("SELECT * FROM daily_calorie_goals WHERE email = :email AND date = :date")
    fun getDailyCalorieGoalByDate(email: String, date: String): DailyCalorieGoal?

    @Query("DELETE FROM daily_calorie_goals WHERE email = :email AND date = :date")
    fun deleteDailyCalorieGoalByDate(email: String, date: String): Int
}
