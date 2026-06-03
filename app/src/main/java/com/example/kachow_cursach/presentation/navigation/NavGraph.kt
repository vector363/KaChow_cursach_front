package com.example.kachow_cursach.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kachow_cursach.domain.model.Dealership
import com.example.kachow_cursach.presentation.screens.login.LoginScreen
import com.example.kachow_cursach.presentation.screens.login.RegisterScreen
import com.example.kachow_cursach.presentation.screens.main.CarDetailScreen
import com.example.kachow_cursach.presentation.screens.main.DealershipSelectionScreen
import com.example.kachow_cursach.presentation.screens.main.MainScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.kachow_cursach.presentation.components.AddCarScreen
import com.example.kachow_cursach.presentation.components.EditCarScreen
import com.example.kachow_cursach.presentation.screens.main.AdminPanelScreen
import com.example.kachow_cursach.di.AppModule


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var selectedDealership by remember { mutableStateOf<Dealership?>(null) }
    val tokenManager = AppModule.getTokenManager()


    NavHost(
        navController = navController,
        startDestination = if (tokenManager.isLoggedIn()) "dealership_selection" else "login"
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
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("dealership_selection") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
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
            CarDetailScreen(navController, carId)
        }

        composable("admin_panel") {
            AdminPanelScreen(navController)
        }

        composable(
            "add_car/{dealershipId}",
            arguments = listOf(navArgument("dealershipId") { type = NavType.IntType })
        ) { backStackEntry ->
            val dealershipId = backStackEntry.arguments?.getInt("dealershipId") ?: 1
            AddCarScreen(navController, dealershipId)
        }

        composable("edit_car/{carId}/{dealershipId}",
            arguments = listOf(
                navArgument("carId") { type = NavType.IntType },
                navArgument("dealershipId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: return@composable
            val dealershipId = backStackEntry.arguments?.getInt("dealershipId") ?: return@composable
            EditCarScreen(navController, carId, dealershipId)
        }
    }
}
