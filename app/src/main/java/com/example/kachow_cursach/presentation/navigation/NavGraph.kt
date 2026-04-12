package com.example.kachow_cursach.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kachow_cursach.R
import com.example.kachow_cursach.domain.model.Car
import com.example.kachow_cursach.presentation.screens.login.LoginScreen
import com.example.kachow_cursach.presentation.screens.login.RegisterScreen
import com.example.kachow_cursach.presentation.screens.main.CarDetailScreen
import com.example.kachow_cursach.presentation.screens.main.MainScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val cars = listOf(
        Car(1, "BMW", "E60", 2225000, "2023", 5000, R.drawable.e60_1webp, false),
        Car(2, "Ford", "Mondeo", 2512000, "2023", 5000, R.drawable.e60_image, false),
        Car(3, "Mitsubishi", "Lancer 9", 252424000, "2023", 5000, R.drawable.e60_1webp, false),
        Car(4, "BMW", "M4 G82", 2502400, "2023", 5000, R.drawable.e60_1webp, false),
    )

    NavHost(
        navController = navController,
        startDestination = "login"
        ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable(
            "car_detail/{carId}",
            arguments = listOf(navArgument("carId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: return@composable
            val car = cars.find { it.id == carId }
            if (car != null) {
                CarDetailScreen(navController, car)
            }
        }
    }
}
