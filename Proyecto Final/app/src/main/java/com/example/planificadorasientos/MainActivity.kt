package com.example.planificadorasientos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planificadorasientos.ui.screens.AdminDashboard
import com.example.planificadorasientos.ui.screens.LoginScreen
import com.example.planificadorasientos.ui.screens.SplashScreen
import com.example.planificadorasientos.ui.screens.StudentDashboard
import com.example.planificadorasientos.ui.theme.PlanificadorAsientosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanificadorAsientosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlanificadorAsientosApp()
                }
            }
        }
    }
}

@Composable
fun PlanificadorAsientosApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"    // 👈 volvemos a usar nuestro Splash composable
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("admin_dashboard") { AdminDashboard(navController = navController) }
        composable("student_dashboard/{id}") { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            StudentDashboard(navController = navController, studentId = id)
        }
    }
}
