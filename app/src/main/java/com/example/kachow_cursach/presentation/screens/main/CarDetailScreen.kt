package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kachow_cursach.R
import com.example.kachow_cursach.data.network.KtorClient
import com.example.kachow_cursach.di.AppModule
import com.example.kachow_cursach.presentation.components.FullScreenImageViewer
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    navController: NavController,
    carId: Int,
    carViewModel: CarViewModel = AppModule.provideCarViewModel()
) {
    println("CarDetailScreen запущен")

    var showFullScreenImage by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    val car by carViewModel.carDetail.collectAsState()
    val isLoading by carViewModel.isLoading.collectAsState()
    val error by carViewModel.error.collectAsState()

    LaunchedEffect(carId) {
        carViewModel.loadCarDetail(carId)
    }

    val carImagesMap by carViewModel.carImages.collectAsState()
    val currentCarImages = remember(carImagesMap[carId]) {
        carImagesMap[carId] ?: emptyList()
    }

    val allImages = remember(currentCarImages) {
        currentCarImages.map { it.imageUrl }.distinct()
    }

    if (showFullScreenImage && allImages.isNotEmpty()) {
        println("все фото: ${allImages}")
        println("клик на фото: ${selectedImageIndex}")
        FullScreenImageViewer(
            images = allImages,
            initialIndex = selectedImageIndex,
            onDismiss = { showFullScreenImage = false }
        )
    }



    Scaffold(
        modifier = Modifier.padding(top = 8.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 15.dp),
                title = {
                    Text(
                        text = "${car?.brand} ${car?.model}, ${car?.year}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
//                actions = {
//                    IconButton(
//                        onClick = {
//                            car?.let {
//                                carViewModel.toggleFavorite(
//                                    carId = it.id,
//                                    isCurrentlyFavorite = it.isFavorite
//                                )
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = if (car?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                            modifier = Modifier.size(26.dp),
//                            contentDescription = "Избранное",
//                            tint = if (car?.isFavorite == true) Color.Red else Color.White
//                        )
//                    }
//                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            LazyRow(
                modifier = Modifier
                    .width(400.dp)
                    .height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(allImages.size) { index ->
                    Box(
                        modifier = Modifier
                            .width(400.dp)
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            .clickable {
                                println("клик на: ${index}")

                                selectedImageIndex = index
                                showFullScreenImage = true
                            }
                    ) {
                        AsyncImage(
                            model = KtorClient.getFullUrl(allImages[index]),
                            contentDescription = "${car?.brand} ${car?.model}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(allImages.size) { index ->
                    Column {
                        Box(
                            modifier = Modifier
                                .size(60.dp, 60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                .clickable {
                                    selectedImageIndex = index
                                    showFullScreenImage = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                            model = KtorClient.getFullUrl(allImages[index]),
                            contentDescription = "${car?.brand} ${car?.model}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${car?.brand} ${car?.model}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${car?.year}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = Modifier.height(70.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column {
                            Text(
                                text = "Цена до скидок:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${car?.price} ₽",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Card(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                        text = "Характеристики",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.icon_engine),
                            contentDescription = "двигатель",
                            modifier = Modifier.size(45.dp),
                            tint = Color.White
                        )
                        Column {
                            Text(
                                text = "${car?.horsepower} л.с.",
                                fontSize = 23.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "${car?.engine}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.icon_transmission),
                            contentDescription = "трансмиссия",
                            modifier = Modifier.size(45.dp),
                            tint = Color.White
                        )
                        Column {
                            Text(
                                text = "${car?.transmission}",
                                fontSize = 23.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "AT",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_fuel),
                            contentDescription = "Тип топлива",
                            modifier = Modifier.size(45.dp),
                            tint = Color.White
                        )

                        Column {
                            Text(
                                text = "Гибрид",
                                fontSize = 23.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "топливо",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.icon_drive),
                            contentDescription = "Привод",
                            modifier = Modifier.size(45.dp),
                            tint = Color.White
                        )

                        Column {
                            Text(
                                text = "${car?.driveUnit}",
                                fontSize = 23.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "4WD",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //  --------------Описание--------------
                Card(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                        text = "Описание",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column (
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "${car?.description}",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Повреждения
                Card(
                    modifier = Modifier
                        .height(400.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ){

                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(top = 10.dp),
                        text = "Повреждения",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        Image(
                            painter = painterResource(id =  R.drawable.scheme_auto),
                            contentDescription = "Схема автомобиля",
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = {
                                selectedImageIndex = 1
                                showFullScreenImage = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_visibility_on),
                                contentDescription = "Вид спереди",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .padding(4.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                selectedImageIndex = 1
                                showFullScreenImage = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .offset(x = -10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_visibility_on),
                                contentDescription = "Вид сзади",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .padding(4.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Купить сейчас",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

