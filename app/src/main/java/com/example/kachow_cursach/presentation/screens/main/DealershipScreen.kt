package com.example.kachow_cursach.presentation.screens.main

import androidx.compose.foundation.Image
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
import com.example.kachow_cursach.R
import com.example.kachow_cursach.domain.model.Dealership
import com.example.kachow_cursach.presentation.theme.primaryContainerColor


data class DealershipWithImage(
    val id: Int,
    val name: String,
    val address: String,
    val imageRes: Int
)

@Composable
fun DealershipSelectionScreen(
    navController: NavController,
    onDealershipSelected: (Dealership) -> Unit
) {
    val dealerships = listOf(
        DealershipWithImage(1, "JetCar", "г. Москва, ул. Ленина, 15", R.drawable.jetcar_preview),
        DealershipWithImage(2, "GearSale", "г. Москва, Московская ул., 42", R.drawable.dealership_preview),
        DealershipWithImage(3, "Rolf", "г. Оренбург, пр. Победы, 8", R.drawable.rolf_preview),
    )

    Scaffold{ paddingValues ->
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(dealerships) { dealership ->
                    DealershipCard(
                        dealership = dealership,
                        onClick = {
                            onDealershipSelected(
                                Dealership(dealership.id, dealership.name, dealership.address)
                            )
                            navController.navigate("main")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DealershipCard(
    dealership: DealershipWithImage,
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
                Image(
                    painter = painterResource(id = dealership.imageRes),
                    contentDescription = "превью автосалона jetCar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                        text = "${dealership.address}",
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
                            text = "кол-во авто: 150",
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
                            text = "рейтинг ⭐ 4.5",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}