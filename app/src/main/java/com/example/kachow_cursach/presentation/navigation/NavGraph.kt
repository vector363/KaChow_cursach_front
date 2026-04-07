package com.example.kachow_cursach.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kachow_cursach.presentation.screens.login.LoginScreen
import com.example.kachow_cursach.presentation.screens.login.RegisterScreen
import com.example.kachow_cursach.presentation.screens.main.CatalogScreen
import com.example.kachow_cursach.presentation.screens.main.MainScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()

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
        composable("catalog") {
            CatalogScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
    }
}
