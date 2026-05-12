package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.kachow_cursach.domain.model.Dealership
import com.example.kachow_cursach.presentation.components.CarItem
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    dealership: Dealership?,
    carViewModel: CarViewModel = AppModule.provideCarViewModel()
)  {
    val cars by carViewModel.cars.collectAsState()
    val isLoading by carViewModel.isLoading.collectAsState()
    val error by carViewModel.error.collectAsState()

    LaunchedEffect(dealership) {
        dealership?.let {
            carViewModel.loadCars(it.id)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ){

                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = {
                            navController.navigate("dealership_selection") {
                                popUpTo("main") { inclusive = true }
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.icon_arrow_left),
                                contentDescription = "Назад к выбору салона",
                                tint = Color.White
                            )
                        }

                        Text(

                            text = "Автокаталог",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (dealership != null) {
                            Text(
                                text = dealership.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(cars) { car ->
                CarItem(
                    car = car,
                    onClick = {
                        navController.navigate("car_detail/${car.id}")
                    },
                    onFavoriteClick = {
                        carViewModel.toggleFavorite(car.id, car.isFavorite) { success ->
                            if (success) {
                                dealership?.let { carViewModel.loadCars(it.id) }
                            }
                        }
                    }
                )
            }
        }
    }
}

