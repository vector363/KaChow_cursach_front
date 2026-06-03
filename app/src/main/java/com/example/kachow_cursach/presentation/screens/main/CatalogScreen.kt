package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.kachow_cursach.presentation.components.FilterDialog
import com.example.kachow_cursach.presentation.model.CarFilters
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    dealership: Dealership?,
    carViewModel: CarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.kachow_cursach.di.CarViewModelFactory(AppModule.mainRepository)
    )
) {
    val cars by carViewModel.cars.collectAsState()
    val isLoading by carViewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(CarFilters()) }

    LaunchedEffect(Unit) {
        dealership?.let {
            carViewModel.loadCars(it.id)
        }
    }

    fun filterCars(carsList: List<com.example.kachow_cursach.data.model.CarDto>): List<com.example.kachow_cursach.data.model.CarDto> {
        return carsList.filter { car ->
            val matchesSearch = searchQuery.isBlank() ||
                    car.brand.contains(searchQuery, ignoreCase = true) ||
                    car.model.contains(searchQuery, ignoreCase = true) ||
                    "${car.brand} ${car.model}".contains(searchQuery, ignoreCase = true)

            if (!matchesSearch) return@filter false

            filters.minPrice?.let { minPrice ->
                val carPrice = car.price ?: return@filter false
                if (carPrice < minPrice) return@filter false
            }
            filters.maxPrice?.let { maxPrice ->
                val carPrice = car.price ?: return@filter false
                if (carPrice > maxPrice) return@filter false
            }

            filters.minYear?.let { if ((car.year ?: 0) < it) return@filter false }
            filters.maxYear?.let { if ((car.year ?: 0) > it) return@filter false }

            filters.minMileage?.let { if ((car.mileage ?: 0) < it) return@filter false }
            filters.maxMileage?.let { if ((car.mileage ?: 0) > it) return@filter false }

            true
        }
    }

    val filteredCars = remember(cars, searchQuery, filters) {
        filterCars(cars)
    }

    val activeFiltersCount = remember(filters) {
        listOf(
            filters.minPrice, filters.maxPrice,
            filters.minYear, filters.maxYear,
            filters.minMileage, filters.maxMileage
        ).count { it != null }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = {
                                navController.navigate("dealership_selection") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        ) {
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
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Загрузка автомобилей...",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Поиск по марке, модели...") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_search),
                                        contentDescription = "Поиск",
                                        tint = Color.White
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

                            BadgedBox(
                                badge = {
                                    if (activeFiltersCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = activeFiltersCount.toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = { showFilterDialog = true },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_filter),
                                        contentDescription = "Фильтры",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    if (activeFiltersCount > 0) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Активные фильтры:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                AssistChip(
                                    onClick = { filters = CarFilters() },
                                    label = { Text("Сбросить все", fontSize = 12.sp) },
                                    modifier = Modifier.wrapContentSize()
                                )
                            }
                        }
                    }

                    if (filteredCars.isEmpty() && (searchQuery.isNotBlank() || filters.isNotEmpty())) {
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
                                        text = "Попробуйте изменить параметры поиска",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    } else if (filteredCars.isNotEmpty()) {
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
                                                carViewModel.toggleFavorite(
                                                    car.id,
                                                    car.isFavorite
                                                ) { success ->
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

    if (showFilterDialog) {
        FilterDialog(
            currentFilters = filters,
            onApply = { newFilters ->
                filters = newFilters
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}