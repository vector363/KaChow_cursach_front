package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kachow_cursach.R
import com.example.kachow_cursach.di.AppModule
import com.example.kachow_cursach.presentation.components.EditCarCard
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navController: NavController,
    carViewModel: CarViewModel = AppModule.provideCarViewModel()
) {
    val favorites by carViewModel.favorites.collectAsState()
    val carImages by carViewModel.carImages.collectAsState()
    val isLoading by carViewModel.isLoading.collectAsState()
    val error by carViewModel.error.collectAsState()


    LaunchedEffect(Unit) {
        carViewModel.loadFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ){
                        Text(
                            text = "Панель редактирования",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = { navController.navigate("add_car/1")
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_add_24),
                                contentDescription = "Добавить авто",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },

    ) { paddingValues ->
        if (favorites.isEmpty()) {
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
                        text = "Добавьте машины для редактирования",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(favorites) { car ->
                    EditCarCard(
                        car = car,
                        images = carImages[car.id] ?: emptyList(),
                        onClick = {
                            navController.navigate("edit_car/${car.id}/${car.dealershipId}")
                        },
                        onFavoriteClick = {
//                            carViewModel.toggleFavorite(car.id, true) { success ->
//                                if (success) {
//
//                                }
//                            }
                        }
                    )
                }
            }
        }
    }
}