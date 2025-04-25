package com.example.finalproject

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FitnessViewModel : ViewModel() {

    // State for AI-generated workout response
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // State for step tracking
    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps.asStateFlow()

    // Gemini Generative AI Model
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    // Increase step count by 1
    fun incrementStep() {
        _steps.value += 1
    }

    // Set step count to a specific value (used for restoring from shared prefs)
    fun updateSteps(newStepCount: Int) {
        _steps.value = newStepCount
    }

    // Request AI-generated workout based on prompt (and optional image)
    fun sendPrompt(
        bitmap: Bitmap? = null,
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val structuredPrompt = """
                    Based on the following request, give a short and clear workout plan.
                    Request: "$prompt"
                    
                    Please format the response like this:

                    Workout for [Body Part]
                    - Exercise 1: x reps
                    - Exercise 2: x reps
                    and make it look like a workout plan for an app and structure it well.
                """.trimIndent()

                val inputContent = content {
                    if (bitmap != null) {
                        image(bitmap)
                    }
                    text(structuredPrompt)
                }

                val response = generativeModel.generateContent(inputContent)

                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                } ?: run {
                    _uiState.value = UiState.Error("No response text found.")
                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }
}
