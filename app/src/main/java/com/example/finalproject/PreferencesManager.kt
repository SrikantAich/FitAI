package com.example.finalproject

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.firestore.FirebaseFirestore
import uploadFitnessData

// SharedPreferences constants
private const val PREF_NAME = "FitnessPrefs"
private const val KEY_STEP_COUNT = "stepCount"
private const val KEY_USER_ID = "userId"
private const val KEY_PASSWORD = "password"
private const val KEY_NAME = "name"
private const val KEY_AGE = "age"
private const val KEY_HEIGHT = "height"
private const val KEY_WEIGHT = "weight"
private const val KEY_DISTANCE = "distance"
private const val KEY_CALORIES = "calories"
private const val KEY_IS_LOGGED_IN = "isLoggedIn"
private const val KEY_GOAL = "goal"

// Save step count and upload to Firebase
fun saveStepCount(context: Context, steps: Int) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt(KEY_STEP_COUNT, steps)
    }
    uploadFitnessData(context)
}

// Get step count
fun getStepCount(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(KEY_STEP_COUNT, 0)
}

// Save distance and calories, then upload
fun saveDistanceAndCalories(context: Context, distance: Float, calories: Int) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putFloat(KEY_DISTANCE, distance)
        putInt(KEY_CALORIES, calories)
        apply()
    }
    uploadFitnessData(context)
}

// Get saved distance
fun getSavedDistance(context: Context): Float {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getFloat(KEY_DISTANCE, 0f)
}

// Get saved calories
fun getSavedCalories(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(KEY_CALORIES, 0)
}

// Save user fitness goal
fun saveGoal(context: Context, goal: Int) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt(KEY_GOAL, goal)
    }
}

// Get saved fitness goal
fun getSavedGoal(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(KEY_GOAL, 1000)
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

// Save user profile details
fun saveUserProfile(context: Context, name: String, age: Int, height: Float, weight: Float) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString(KEY_NAME, name)
        putInt(KEY_AGE, age)
        putFloat(KEY_HEIGHT, height)
        putFloat(KEY_WEIGHT, weight)
    }
    uploadFitnessData(context)
}

// Retrieve stored user profile details
fun getUserProfile(context: Context): UserProfile {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val name = sharedPreferences.getString(KEY_NAME, "") ?: ""
    val age = sharedPreferences.getInt(KEY_AGE, 0)
    val height = sharedPreferences.getFloat(KEY_HEIGHT, 0f)
    val weight = sharedPreferences.getFloat(KEY_WEIGHT, 0f)

    return UserProfile(name, age, height, weight)
}

// Clear all saved preferences
fun clearAllPreferences(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        clear()
    }
}

// Clear user profile data
fun clearUserProfile(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_NAME)
        remove(KEY_AGE)
        remove(KEY_HEIGHT)
        remove(KEY_WEIGHT)
    }
}

// Clear credentials
fun clearCredentials(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_USER_ID)
        remove(KEY_PASSWORD)
    }
}

// Save login status
fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }
}

// Check login status
fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
}

// Logout user
fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        remove(KEY_IS_LOGGED_IN)
    }
}

// Gather all fitness data into one object
fun getAllFitnessData(context: Context): FitnessData {
    val userProfile = getUserProfile(context)
    val steps = getStepCount(context)
    val calories = getSavedCalories(context)
    val distance = getSavedDistance(context)
    val goal = getSavedGoal(context)

    return FitnessData(
        name = userProfile.name,
        age = userProfile.age,
        height = userProfile.height,
        weight = userProfile.weight,
        steps = steps,
        calories = calories,
        distance = distance,
        goal = goal
    )
}
