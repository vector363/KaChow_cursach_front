package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    carViewModel: CarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.kachow_cursach.di.CarViewModelFactory(AppModule.mainRepository)
    )
)  {
    val cars by carViewModel.cars.collectAsState()
    val isLoading by carViewModel.isLoading.collectAsState()
    val error by carViewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dealership?.let {
            carViewModel.loadCars(it.id)
        }
    }

    val filteredCars = if (searchQuery.isBlank()) {
        cars
    } else {
        cars.filter { car ->
            car.brand.contains(searchQuery, ignoreCase = true) ||
                    car.model.contains(searchQuery, ignoreCase = true) ||
                    "${car.brand} ${car.model}".contains(searchQuery, ignoreCase = true)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Поиск по марке или модели...") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.icon_visibility_on),
                                contentDescription = "Поиск",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_close),
                                        contentDescription = "Очистить",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        ),
                        singleLine = true
                    )
                }

                if (filteredCars.isEmpty() && searchQuery.isNotBlank()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(600.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_visibility_on),
                                    contentDescription = "Ничего не найдено",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "Ничего не найдено",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Попробуйте изменить поисковый запрос",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                } else {
                    val chunkedCars = filteredCars.chunked(2)
                    items(chunkedCars) { rowCars ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowCars.forEach { car ->
                                Box(modifier = Modifier.weight(1f)) {
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
                            if (rowCars.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}