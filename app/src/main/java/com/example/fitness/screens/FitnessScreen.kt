package com.example.fitness.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * FitnessScreen composable displays the user's calorie tracking and macronutrient goals.
 * It also provides a placeholder for exercise records when no data is available.
 */
@Composable
fun FitnessScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Row with Calorie burn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Calorie burned", fontSize = 14.sp, color = Color.Gray)
                Text(text = "0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "kcal", fontSize = 12.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Remaining", fontSize = 14.sp, color = Color.Gray)
                Text(text = "2000", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Recommended", fontSize = 12.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Calorie Intake", fontSize = 14.sp, color = Color.Gray)
                Text(text = "0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "kcal", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Macronutrient Goals
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutrientGoal("Carbs", "0 / 183g")
            NutrientGoal("Protein", "0 / 73g")
            NutrientGoal("Fat", "0 / 49g")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // No Records Placeholder
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No exercise Records", fontSize = 16.sp, color = Color.Gray)
            Text(text = "Please log exercise below for accurate analysis", fontSize = 12.sp, color = Color.Gray)
        }


    }
}
