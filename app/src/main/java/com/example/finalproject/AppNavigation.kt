import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.finalproject.FitnessScreen
import com.example.finalproject.LoginScreen
import com.example.finalproject.ProfilePage
import com.example.finalproject.SignUpDetailScreen
import com.example.finalproject.SignupScreen



@Composable
fun AppNavigation(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("fitnessScreen") {
            FitnessScreen(navController = navController)
        }
        composable("stepsDetails") {
            StepsScreen(navController = navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("signUpDetailScreen") {
            SignUpDetailScreen(navController = navController)
        }
        composable("profilePage") {
            ProfilePage()
        }
        composable("caloriesDetails") {
            CaloriesDetailsScreen()
        }
        composable("AIWorkoutPlanner") {
            AIWorkoutPlannerScreen()
        }
    }
}
