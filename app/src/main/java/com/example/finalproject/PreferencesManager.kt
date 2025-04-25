package com.example.finalproject

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

// SharedPreferences constants
private const val PREF_NAME = "FitnessPrefs"
private const val KEY_STEP_COUNT = "stepCount"
private const val KEY_USER_ID = "userId"
private const val KEY_PASSWORD = "password"
private const val KEY_NAME = "name"
private const val KEY_AGE = "age"
private const val KEY_HEIGHT = "height"
private const val KEY_WEIGHT = "weight"
private const val KEY_DISTANCE = "distance"  // Added for distance
private const val KEY_CALORIES = "calories"
private const val KEY_IS_LOGGED_IN = "isLoggedIn"


fun saveDistanceAndCalories(context: Context, distance: Float, calories: Int) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Save the data using the editor returned by the edit block
    sharedPreferences.edit().apply {
        putFloat(KEY_DISTANCE, distance)  // Save distance
        putInt(KEY_CALORIES, calories)    // Save calories
        apply() // Apply changes
    }
}
fun getSavedDistance(context: Context): Float {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getFloat(KEY_DISTANCE, 0f) // Default to 0f if not found
}

fun getSavedCalories(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(KEY_CALORIES, 0) // Default to 0 if not found
}

fun saveGoal(context: Context, goal: Int) {
    val sharedPreferences = context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit() {
        putInt("goal", goal)
    }
}

fun getSavedGoal(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("goal", 1000) // Default goal if not set
}

// Save step count
fun saveStepCount(context: Context, steps: Int) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt(KEY_STEP_COUNT, steps)
    }
}

// Get step count
fun getStepCount(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(KEY_STEP_COUNT, 0)
}

// Save login credentials
fun saveCredentials(context: Context, userId: String, password: String) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString(KEY_USER_ID, userId)
        putString(KEY_PASSWORD, password)
    }
}

// Retrieve stored credentials
fun getStoredCredentials(context: Context): Pair<String?, String?> {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString(KEY_USER_ID, null)
    val password = sharedPreferences.getString(KEY_PASSWORD, null)
    return Pair(userId, password)
}

// Save user profile details (name, age, height, weight)
// Save user profile details (name, age, height, weight)
fun saveUserProfile(context: Context, name: String, age: Int, height: Float, weight: Float) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString(KEY_NAME, name)
        putInt(KEY_AGE, age)
        putFloat(KEY_HEIGHT, height)
        putFloat(KEY_WEIGHT, weight)
    }
}


// Retrieve stored user profile details (name, age, height, weight)
fun getUserProfile(context: Context): UserProfile {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val name = sharedPreferences.getString(KEY_NAME, "") ?: ""
    val age = sharedPreferences.getInt(KEY_AGE, 0)
    val height = sharedPreferences.getFloat(KEY_HEIGHT, 0f)
    val weight = sharedPreferences.getFloat(KEY_WEIGHT, 0f)

    return UserProfile(name, age, height, weight) // Return the custom UserProfile object
}

// Clear all saved preferences (optional utility)
fun clearAllPreferences(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        clear()
    }
}

// Clear specific user profile data (optional utility)
fun clearUserProfile(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_NAME)
        remove(KEY_AGE)
        remove(KEY_HEIGHT)
        remove(KEY_WEIGHT)
    }
}

// Clear credentials (optional utility)
fun clearCredentials(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_USER_ID)
        remove(KEY_PASSWORD)
    }
}
// -------------------- Login Status --------------------
fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }
}

fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
}

fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_IS_LOGGED_IN)
    }
}
