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
import com.example.kachow_cursach.presentation.screens.main.AdminPanelScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    var selectedDealership by remember { mutableStateOf<Dealership?>(null) }


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
            CarDetailScreen(navController, carId)
        }

        composable("admin_panel") {
            AdminPanelScreen(navController)
        }
    }
}
