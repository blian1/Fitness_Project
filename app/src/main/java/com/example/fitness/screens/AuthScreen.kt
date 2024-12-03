package com.example.fitness.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitness.database.AppDatabase
import com.example.fitness.database.User
import com.example.fitness.database.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Composable function for the authentication screen.
 * This screen allows users to log in or register using email/password or Google Sign-In.
 *
 * @param onLoginSuccess Callback triggered when the login is successful.
 * @param onProfileInput Callback triggered when the user needs to input their profile information.
 * @param navController Navigation controller for managing navigation between screens.
 */
@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit,
    onProfileInput: (String) -> Unit,
    navController: NavHostController
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).userDao()
    val scope = rememberCoroutineScope()

    // Configure Google Sign-In options
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // Initialize Google Sign-In launcher
    val googleSignInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                scope.launch {
                    handleGoogleSignIn(account, db, onLoginSuccess, onProfileInput, context)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Login" else "Register",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Submit button for Login or Register
        Button(
            onClick = {
                scope.launch {
                    handleAuthAction(
                        isLogin = isLogin,
                        email = email,
                        password = password,
                        db = db,
                        context = context,
                        onLoginSuccess = onLoginSuccess,
                        onProfileInput = onProfileInput
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Login" else "Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Google Sign-In button
        Button(
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between Login and Register
        TextButton(onClick = { isLogin = !isLogin }) {
            Text(if (isLogin) "Don't have an account? Register" else "Already have an account? Login")
        }
    }
}

/**
 * Handles the logic for login or registration based on the input state.
 */
private suspend fun handleAuthAction(
    isLogin: Boolean,
    email: String,
    password: String,
    db: UserDao,
    context: Context,
    onLoginSuccess: (String) -> Unit,
    onProfileInput: (String) -> Unit
) {
    try {
        if (isLogin) {
            val user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
            if (user != null && user.password == password) {
                val isComplete = withContext(Dispatchers.IO) { isProfileComplete(email, db) }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    if (isComplete) onLoginSuccess(email) else onProfileInput(email)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            if (email.isBlank() || password.isBlank()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.IO) {
                    db.insertUser(User(email = email, password = password))
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

/**
 * Handles the logic for login or registration based on the input state.
 */
private suspend fun handleGoogleSignIn(
    account: GoogleSignInAccount?,
    db: UserDao,
    onLoginSuccess: (String) -> Unit,
    onProfileInput: (String) -> Unit,
    context: Context
) {
    if (account != null) {
        val email = account.email ?: "Unknown"
        val user = withContext(Dispatchers.IO) {
            db.getUserByEmail(email) ?: run {
                val newUser = User(email = email, password = "")
                db.insertUser(newUser)
                newUser
            }
        }
        val isComplete = withContext(Dispatchers.IO) { isProfileComplete(email, db) }
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Welcome ${account.displayName}", Toast.LENGTH_SHORT).show()
            if (isComplete) onLoginSuccess(email) else onProfileInput(email)
        }
    } else {
        Toast.makeText(context, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Checks if the user's profile is complete by verifying if height and weight are populated.
 */
suspend fun isProfileComplete(email: String, db: UserDao): Boolean {
    val user = withContext(Dispatchers.IO) { db.getUserByEmail(email) }
    return user?.height != null && user.weight != null
}

