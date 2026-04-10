package com.example.kachow_cursach.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.kachow_cursach.R


@Composable
fun FullScreenImageViewer(
    images: List<Int>,
    initialIndex: Int = 0,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }
    var resetZoomTrigger by remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onDismiss() },
                        onDoubleTap = { resetZoomTrigger++ }
                    )
                }

        ) {
            ZoomableImage(
                imageRes = images[currentIndex],
                modifier = Modifier.fillMaxSize(),
                resetTrigger = resetZoomTrigger
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 24.dp, end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "Закрыть",
                    tint = Color.White
                )
            }

            if (images.size > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .size(48.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                currentIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
                                resetZoomTrigger++
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_left),
                        contentDescription = "Назад",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)
                        .size(48.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                currentIndex = if (currentIndex < images.size - 1) currentIndex + 1 else 0
                                resetZoomTrigger++
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = "Вперед",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / ${images.size}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}