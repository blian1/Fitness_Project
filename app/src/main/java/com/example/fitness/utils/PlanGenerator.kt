package com.example.fitness.utils

import com.example.fitness.database.FitnessPlan
import com.example.fitness.database.DietPlan
import com.example.fitness.database.DailyCalorieGoal
import com.example.fitness.API.ChatGPTRequest
import com.example.fitness.API.ChatGPTResponse
import com.example.fitness.API.Message
import com.example.fitness.API.OpenAIApiService
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 * Generates daily calorie goals and plans (fitness and diet) based on user details.
 *
 * @param email User's email
 * @param age User's age
 * @param weight User's weight (kg)
 * @param height User's height (m)
 * @param goal User's fitness goal ("gain muscle" or "lose weight")
 * @return Triple of calorie goal, fitness plans, and diet plans
 */
suspend fun generateDailyPlan(
    email: String,
    age: Int,
    weight: Float,
    height: Float,
    goal: String
): Triple<DailyCalorieGoal, List<FitnessPlan>, List<DietPlan>> {
    val apiService = OpenAIApiService.create()

    // Construct the request to ChatGPT
    val request = ChatGPTRequest(
        model = "gpt-4",
        messages = listOf(
            Message("system", "You are a fitness and diet planner."),
            Message(
                "user", """
                Based on the following user details:
                - Email: $email
                - Age: $age
                - Weight: $weight kg
                - Height: $height m
                - Goal: $goal 
                
                Generate:
                1. Recommended daily calorie intake and burn values.
                2. A fitness plan for the day, including warmup, strength training, cardio, and stretching (if needed).
                3. A diet plan for the day, including breakfast, lunch, dinner, and optional snacks.
                4.Ensure the calorie intake is below the daily maintenance level for weight loss.
                5.Ensure "calorie_goal" is always an object with "intakeCalories" and "burnCalories" as keys, both being integers.
                
                Return a JSON object with the following structure:
                {
                    "calorie_goal": {
                        "intakeCalories": <int>,
                        "burnCalories": <int>
                    },
                    "fitness_plan": [
                        {"type": "warmup", "content": "string", "calories": <int>},
                        {"type": "strength_training", "content": "string", "calories": <int>}
                    ],
                    "diet_plan": [
                        {"type": "breakfast", "content": "string", "calories": <int>},
                        {"type": "lunch", "content": "string", "calories": <int>}
                    ]
                }
                Ensure that calorie values match the user's goal.
            """.trimIndent()
            )
        )
    )


    // Call OpenAI API
    return withContext(Dispatchers.IO) {
        val response: ChatGPTResponse = apiService.getPlan(request)

        // Extract content from the response
        val content = response.choices.firstOrNull()?.message?.content ?: ""
        parsePlanResponse(email, content)
    }

}

/**
 * Parses the ChatGPT API response and converts it to database-compatible objects.
 *
 * @param email User's email
 * @param responseContent API response content
 * @return Triple of calorie goal, fitness plans, and diet plans
 */
fun parsePlanResponse(
    email: String,
    responseContent: String
): Triple<DailyCalorieGoal, List<FitnessPlan>, List<DietPlan>> {

    val gson = Gson()


    val currentDate = LocalDate.now().toString()


    val jsonResponse = gson.fromJson(responseContent, JsonObject::class.java)


    val calorieGoalJson = jsonResponse.getAsJsonObject("calorie_goal")
    val calorieGoal = DailyCalorieGoal(
        email = email,
        date = currentDate,
        intake = calorieGoalJson.get("intakeCalories").asFloat,
        burn = calorieGoalJson.get("burnCalories").asFloat
    )


    val fitnessPlanType = object : TypeToken<List<FitnessPlan>>() {}.type
    val fitnessPlans = gson.fromJson<List<FitnessPlan>>(
        jsonResponse.getAsJsonArray("fitness_plan"),
        fitnessPlanType
    ).map {
        it.copy(email = email, date = currentDate)
    }


    val dietPlanType = object : TypeToken<List<DietPlan>>() {}.type
    val dietPlans = gson.fromJson<List<DietPlan>>(
        jsonResponse.getAsJsonArray("diet_plan"),
        dietPlanType
    ).map {
        it.copy(email = email, date = currentDate)
    }

    return Triple(calorieGoal, fitnessPlans, dietPlans)
}

