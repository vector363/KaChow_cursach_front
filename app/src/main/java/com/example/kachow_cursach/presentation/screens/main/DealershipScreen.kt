package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kachow_cursach.R
import com.example.kachow_cursach.data.model.DealershipDto
import com.example.kachow_cursach.data.network.KtorClient
import com.example.kachow_cursach.di.AppModule
import com.example.kachow_cursach.domain.model.Dealership
import com.example.kachow_cursach.presentation.theme.primaryContainerColor
import com.example.kachow_cursach.presentation.viewmodel.DealershipViewModel


@Composable
fun DealershipSelectionScreen(
    navController: NavController,
    onDealershipSelected: (Dealership) -> Unit,
    viewModel: DealershipViewModel = AppModule.provideDealershipViewModel()
) {
    val dealerships by viewModel.dealerships.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDealerships()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 15.dp)
        ) {
            Text(
                text = "Автосалоны",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadDealerships() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }
                dealerships.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Нет доступных автосалонов")
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(dealerships) { dealership ->
                            DealershipCard(
                                dealership = dealership,
                                onClick = {
                                    onDealershipSelected(
                                        Dealership(
                                            id = dealership.id,
                                            name = dealership.name,
                                            address = dealership.address,
                                            imageUrl = dealership.imageUrl
                                        )
                                    )
                                    navController.navigate("main") {
                                        popUpTo("dealership_selection") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DealershipCard(
    dealership: DealershipDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                if (dealership.imageUrl != null) {
                    val fullUrl = KtorClient.getFullUrl(dealership.imageUrl ?: "")
                    println(">>> Loading image from: $fullUrl")

                    AsyncImage(
                        model = fullUrl,
                        contentDescription = "Фото салона ${dealership.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.dealership_preview)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("фото отсутствует", fontSize = 48.sp)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = dealership.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dealership.address,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = primaryContainerColor
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "кол-во авто: ${dealership.carCount}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = primaryContainerColor
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "рейтинг ⭐ ${dealership.rating}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}