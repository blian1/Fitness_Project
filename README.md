AI Fitness Assistant App

Overview

The AI Fitness Assistant App is a mobile application designed to help users track their fitness goals, create personalized fitness and diet plans, and monitor daily progress. The app leverages advanced AI features for recommendations and insights, ensuring a tailored fitness journey for each user.

Key Features:

User Profile Management: Users can input and manage their personal information such as age, weight, height, and fitness goals.

Fitness Tracking: Track calorie burn, intake, and remaining calories based on user activities.

Diet Planning: Create and monitor personalized diet plans, including calorie and macronutrient goals.

AI Assistance: Interactive AI-powered assistant for fitness tips and queries.

Data Visualization: Graphical insights into progress using detailed charts.

Project Folder Structure

com.example.fitness/
├── auth/            # User authentication logic and API integrations.
├── data/            # Handles data retrieval and storage.
│   ├── api/         # APIs for retrieving data.
│   └── models/      # Data classes and structures.
├── ui/              # Components for UI and screens.
│   ├── components/  # Reusable UI elements.
│   ├── screens/     # Individual pages of the app.
│   └── activities/  # Non-MainActivity activities.
└── utils/           # Utility classes and functions.

Folder Details

auth/: Handles user authentication, including sign-in and sign-up functionalities.

data/api/: Includes the API interface and methods for handling API calls.

data/models/: Contains data models for user profiles, fitness plans, diet plans, and API responses.

ui/components/: Includes reusable UI components such as buttons and dialogs.

ui/screens/: Houses screens like Home, Fitness, Diet, AI Assistant, and Calendar.

utils/: Contains helper classes for functions like date formatting and API key management.

UI Screens

HomeScreen: Displays user profile information and daily plans.

FitnessScreen: Tracks calorie burn, intake, and macronutrient goals.

DietScreen: Logs and displays daily diet plans and macronutrient tracking.

AIScreen: Interactive chat interface for AI-powered fitness assistance.

CalendarScreen: Generates and displays daily fitness and diet plans.

Prerequisites

To run this application, ensure the following are set up on your system:

Requirements:

Android Studio (latest version)

Minimum API level 21 (Android 5.0 Lollipop)

Internet connection

How to Run the Application

Clone the Repository:

git clone https://github.com/your-repository/ai-fitness-assistant.git
cd ai-fitness-assistant

Open in Android Studio:

Launch Android Studio.

Open the project folder.

Add API Keys:

Navigate to res/values/strings.xml.

Replace the placeholders for the API keys with your own keys.

<string name="openai_api_key">YOUR_OPENAI_API_KEY</string>

Run the App:

Connect your Android device or use an emulator.

Click on the green play button in Android Studio to build and run the app.

API Key Registration

OpenAI API

Visit the OpenAI API.

Sign up or log in to your OpenAI account.

Navigate to the API keys section and generate a new API key.

Replace the placeholder in strings.xml as shown above.

Additional API Integrations (if applicable):

Unsplash API for Images:

Register at Unsplash Developers.

Replace the placeholder key in the strings.xml file.

Accessibility

The app will be regularly tested and improved to enhance accessibility. It currently meets basic accessibility standards, but ongoing efforts will address all identified issues.

Future Enhancements

Integration with fitness devices (e.g., Fitbit, Apple Watch).

Advanced AI analytics for more detailed fitness insights.

Offline mode for tracking activities without an internet connection.

Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request.

License

This project is licensed under the MIT License. See the LICENSE file for details.

Contact

For support or inquiries, please contact your-email@example.com.

