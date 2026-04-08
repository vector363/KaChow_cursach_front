package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.kachow_cursach.domain.model.Car
import com.example.kachow_cursach.R
import com.example.kachow_cursach.presentation.components.CarItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController) {

    val cars = listOf(
        Car(
            id = 1,
            brand = "BMW",
            model = "E60",
            price = 25000,
            year = "2023",
            mileage = 5000,
            imageRes = R.drawable.e60_image,
            isFavorite = false
        ),
        Car(
            id = 2,
            brand = "Mitsubishi",
            model = "Evolution 9",
            price = 45000,
            year = "2022",
            mileage = 3000,
            imageRes = R.drawable.evo_9_image,
            isFavorite = true
        ),
        Car(
            id = 3,
            brand = "BMW",
            model = "M4G82",
            price = 205000,
            year = "2023",
            mileage = 5000,
            imageRes = R.drawable.m4_bmw,
            isFavorite = false
        ),
        Car(
            id = 4,
            brand = "BMW",
            model = "E60",
            price = 4500000,
            year = "2022",
            mileage = 3000,
            imageRes = R.drawable.alfa_romeo,
            isFavorite = true
        ),
        Car(
            id = 5,
            brand = "BMW",
            model = "M4G82",
            price = 25000000,
            year = "2023",
            mileage = 5000,
            imageRes = R.drawable.e60_image,
            isFavorite = false
        ),
        Car(
            id = 6,
            brand = "BMW",
            model = "E60",
            price = 4532000,
            year = "2022",
            mileage = 3000,
            imageRes = R.drawable.alfa_romeo,
            isFavorite = true
        ),
        Car(
            id = 7,
            brand = "Cadillac",
            model = "Escalade",
            price = 2504300,
            year = "2023",
            mileage = 5000,
            imageRes = R.drawable.e60_image,
            isFavorite = false
        ),
        Car(
            id = 8,
            brand = "Mersedec-Benz",
            model = "E63 AMG",
            price = 4503400,
            year = "2022",
            mileage = 3233000,
            imageRes = null,
            isFavorite = true
        ),
        Car(
            id = 9,
            brand = "Cadillac",
            model = "Escalade",
            price = 2504300,
            year = "2023",
            mileage = 53232000,
            imageRes = R.drawable.m4_bmw,
            isFavorite = false
        ),
        Car(
            id = 10,
            brand = "Mersedec-Benz",
            model = "E63 AMG",
            price = 4503400,
            year = "2022",
            mileage = 3000,
            imageRes = null,
            isFavorite = true
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Автокаталог",
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
                        // переход на детальный экран
                    },
                    onFavoriteClick = {
                        // обновление избранного
                    }
                )
            }
        }
    }
}

