package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kachow_cursach.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
    val selectedIcon: Int
) {
    object Catalog : BottomNavItem(
        route = "catalog",
        title = "Каталог",
        icon = R.drawable.icon_account,
        selectedIcon = R.drawable.icon_visibility_off
    )
    object Favorites : BottomNavItem(
        route = "favorites",
        title = "Избранное",
        icon = R.drawable.icon_account,
        selectedIcon = R.drawable.icon_visibility_off
    )
    object Profile : BottomNavItem(
        route = "profile",
        title = "Профиль",
        icon = R.drawable.icon_account,
        selectedIcon = R.drawable.icon_visibility_off
    )
}

@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Catalog) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                val items = listOf(
                    BottomNavItem.Catalog,
                    BottomNavItem.Favorites,
                    BottomNavItem.Profile
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item,
                        onClick = { selectedItem = item },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (selectedItem == item) item.selectedIcon else item.icon
                                ),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                BottomNavItem.Catalog -> CatalogScreen(navController)
                BottomNavItem.Favorites -> FavoritesScreen()
                BottomNavItem.Profile -> ProfileScreen()
            }
        }
    }
}