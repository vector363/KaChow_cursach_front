package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kachow_cursach.R
import com.example.kachow_cursach.domain.model.Car
import com.example.kachow_cursach.presentation.components.FavoriteCarCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {

    var favoriteCars by remember {
        mutableStateOf(
            listOf(
                Car(2, "Mitsubishi", "Evolution 9", 4544000, "2022", 333000, R.drawable.m4_bmw, true),
                Car(7, "Porsche", "911 Turbo", 7440000, "2024", 523200, R.drawable.e60_image, true),
                Car(5, "Mercedes", "E63 AMG", 6524000, "2022", 63242000, R.drawable.alfa_romeo, true),
            )
        )
    }
//    var favoriteCars by remember {mutableStateOf(emptyList<Car>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Избранное",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
    ) { paddingValues ->
        if (favoriteCars.isEmpty()) {
            if (favoriteCars.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(80.dp),
                            painter = painterResource(
                                id = R.drawable.icon_favorite_switch
                            ),
                            contentDescription = "Избранное",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Нет избранных автомобилей",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Добавьте машины через сердечко",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(favoriteCars) { car ->
                    FavoriteCarCard(
                        car = car,
                        onClick = {
                        },
                        onFavoriteClick = {
                        }
                    )
                }
            }
        }
    }
}