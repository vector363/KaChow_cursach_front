package com.example.kachow_cursach.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kachow_cursach.R
import com.example.kachow_cursach.domain.model.Car
import com.example.kachow_cursach.domain.model.Dealership
import com.example.kachow_cursach.presentation.screens.login.LoginScreen
import com.example.kachow_cursach.presentation.screens.login.RegisterScreen
import com.example.kachow_cursach.presentation.screens.main.CarDetailScreen
import com.example.kachow_cursach.presentation.screens.main.DealershipSelectionScreen
import com.example.kachow_cursach.presentation.screens.main.MainScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    // Временное хранилище для выбранного салона
    var selectedDealership by remember { mutableStateOf<Dealership?>(null) }

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

        composable("dealership_selection") {
            DealershipSelectionScreen(
                navController = navController,
                onDealershipSelected = { dealership ->
                    selectedDealership = dealership
                    navController.navigate("main") {
                        popUpTo("dealership_selection") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("main") {
            MainScreen(
                navController,
                dealership = selectedDealership
            )
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
