# AI Fitness Assistant App

The AI Fitness Assistant is a mobile application designed to help users achieve their fitness goals by providing personalized fitness plans, diet plans, and real-time AI assistance.

---

## Features

- **Personalized Fitness Plans**: Users can input their details (age, height, weight, goal) to generate customized fitness and diet plans.
- **Diet Tracking**: Log calorie intake and track macronutrients (carbs, protein, fat).
- **AI Chat Assistant**: Provides instant answers to fitness-related queries.
- **Calendar View**: Displays daily plans and past records.


---

## Project Structure

```plaintext
com.example.fitness/
├── auth/                  # Authentication logic
├── data/                  # Data models and database access
│   ├── api/               # API-related classes
│   └── models/            # Data models
├── ui/                    # UI components and screens
│   ├── activities/        # Android activities
│   ├── components/        # Reusable UI components
│   ├── screens/           # Screens for different features
├── utils/                 # Utility classes and functions
└── README.md              # Documentation
```

### Folders and their Purpose

- **auth/**: Handles user authentication, including sign-in and sign-up functionalities.
- **data/**: Manages the application data and includes models and API-related classes.
  - **api/**: Contains API integrations and logic for external data.
  - **models/**: Data classes representing app entities like `User`, `FitnessPlan`, and `DietPlan`.
- **ui/**: Contains all UI components and screens.
  - **activities/**: Contains app activities (e.g., main activity).
  - **components/**: Reusable content blocks like navigation bars and cards.
  - **screens/**: Individual screens for the app, such as Home, Fitness, and Calendar.
- **utils/**: Contains helper functions and common utilities.

---

## How to Run the App

### Prerequisites

1. **Android Studio**: Ensure you have Android Studio installed on your system.
2. **API Keys**: The app integrates with OpenAI GPT API. Obtain your API key following the steps below.
3. **Emulator/Device**: A compatible Android emulator or physical device for testing.

### Steps to Run

1. Clone the repository:
   ```bash
   git clone <[repository-url](https://github.com/blian1/Fitness_Project.git)>
   ```
2. Open the project in Android Studio.
3. Replace the API keys (details in the next section).
4. Sync the project with Gradle.
5. Run the app on an emulator or physical device.

---

## Setting Up API Keys

### OpenAI GPT API

1. Go to the [OpenAI API website](https://platform.openai.com/signup/).
2. Sign up or log in to your account.
3. Navigate to the API Keys section and create a new API key.
4. Copy the generated API key.
5. In the project, locate the `OpenAIApiServices` file and replace `Authorization: Bearer YOUR API KEY` with your API key:



## Accessibility

The app has been tested with accessibility tools, such as the Accessibility Scanner. Suggestions are being addressed iteratively to improve the user experience.

---

## API Integrations

- **OpenAI GPT API**: Provides AI chat responses.

---

## UI Screens

- **HomeScreen**: Displays user profile and daily plans.
- **FitnessScreen**: Tracks calorie burn, remaining calories, and macronutrients.
- **DietScreen**: Manages calorie intake and macronutrient goals.
- **AIScreen**: AI chat assistant for fitness-related queries.
- **CalendarScreen**: Displays daily plans and historical data.

---



