package com.example.kachow_cursach.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.kachow_cursach.presentation.model.CarFilters


@Composable
fun FilterDialog(
    currentFilters: CarFilters,
    onApply: (CarFilters) -> Unit,
    onDismiss: () -> Unit
) {
    var minPrice by remember { mutableStateOf(currentFilters.minPrice?.toString() ?: "") }
    var maxPrice by remember { mutableStateOf(currentFilters.maxPrice?.toString() ?: "") }
    var minYear by remember { mutableStateOf(currentFilters.minYear?.toString() ?: "") }
    var maxYear by remember { mutableStateOf(currentFilters.maxYear?.toString() ?: "") }
    var minMileage by remember { mutableStateOf(currentFilters.minMileage?.toString() ?: "") }
    var maxMileage by remember { mutableStateOf(currentFilters.maxMileage?.toString() ?: "") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фильтры",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = "Цена (₽)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = minPrice,
                                onValueChange = { minPrice = it.filter { it.isDigit() } },
                                label = { Text("от") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = maxPrice,
                                onValueChange = { maxPrice = it.filter { it.isDigit() } },
                                label = { Text("до") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Год выпуска",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = minYear,
                                onValueChange = { minYear = it.filter { it.isDigit() } },
                                label = { Text("от") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = maxYear,
                                onValueChange = { maxYear = it.filter { it.isDigit() } },
                                label = { Text("до") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Пробег (км)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = minMileage,
                                onValueChange = { minMileage = it.filter { it.isDigit() } },
                                label = { Text("от") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = maxMileage,
                                onValueChange = { maxMileage = it.filter { it.isDigit() } },
                                label = { Text("до") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )

                    ) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = {
                            val filters = CarFilters(
                                minPrice = minPrice.toIntOrNull(),
                                maxPrice = maxPrice.toIntOrNull(),
                                minYear = minYear.toIntOrNull(),
                                maxYear = maxYear.toIntOrNull(),
                                minMileage = minMileage.toIntOrNull(),
                                maxMileage = maxMileage.toIntOrNull()
                            )
                            onApply(filters)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Применить")
                    }
                }
            }
        }
    }
}