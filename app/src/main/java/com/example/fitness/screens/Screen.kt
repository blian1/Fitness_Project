package com.example.fitness.screens

import androidx.annotation.DrawableRes
import com.example.fitness.R


sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Fitness : Screen("fitness", "Fitness", R.drawable.ic_fitness)
    object Diet : Screen("diet", "Diet", R.drawable.ic_diet)
    object AIHelper : Screen("ai_helper", "AI", R.drawable.ic_ai_helper)
    object Calendar : Screen("calendar", "Calendar", R.drawable.ic_calendar)
}