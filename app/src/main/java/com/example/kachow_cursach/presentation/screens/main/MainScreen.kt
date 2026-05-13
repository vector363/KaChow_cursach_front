package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kachow_cursach.R
import com.example.kachow_cursach.di.AppModule
import com.example.kachow_cursach.domain.model.Dealership


sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
    val selectedIcon: Int
) {
    object Catalog : BottomNavItem(
        route = "catalog",
        title = "Каталог",
        icon = R.drawable.icon_caralog_unswirch,
        selectedIcon = R.drawable.icon_caralog_switch
    )
    object Favorites : BottomNavItem(
        route = "favorites",
        title = "Избранное",
        icon = R.drawable.icon_favorite_unswitch,
        selectedIcon = R.drawable.icon_favorite_switch
    )
    object AdminPanel : BottomNavItem(
        route = "admin_panel",
        title = "Управление",
        icon = R.drawable.baseline_settings_24,
        selectedIcon = R.drawable.baseline_settings_24
    )
    object Profile : BottomNavItem(
        route = "profile",
        title = "Профиль",
        icon = R.drawable.icon_profile_unswitch,
        selectedIcon = R.drawable.icon_profile_switch
    )
}


@Composable
fun MainScreen(
    navController: NavController,
    dealership: Dealership? = null
) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Catalog) }

    val authViewModel = AppModule.provideAuthViewModel()
    val isAdmin by remember { mutableStateOf(authViewModel.isAdmin()) }

    val navItems = if (isAdmin) {
        listOf(
            BottomNavItem.Catalog,
            BottomNavItem.AdminPanel,
            BottomNavItem.Profile
        )
    } else {
        listOf(
            BottomNavItem.Catalog,
            BottomNavItem.Favorites,
            BottomNavItem.Profile
        )
    }

    Box(
        modifier = Modifier
            .padding(top = 14.dp)
            .fillMaxSize()

    ) {
        when (selectedItem) {
            BottomNavItem.Catalog -> CatalogScreen(navController, dealership = dealership)
            BottomNavItem.Favorites -> FavoritesScreen(navController)
            BottomNavItem.AdminPanel -> AdminPanelScreen(navController)
            BottomNavItem.Profile -> ProfileScreen(navController)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .padding(bottom = 15.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(horizontal = 12.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(28.dp),
                        clip = false
                    ),
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    navItems.forEach { item ->
                        val isSelected = selectedItem == item

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedItem = item },
                            icon = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (isSelected) item.selectedIcon else item.icon
                                        ),
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (isSelected)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )

                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    }
}