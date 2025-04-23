package com.example.weatherapp.ui.theme.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val dismissThreshold = 200f

    LaunchedEffect(item) {
        offsetX.snapTo(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        DeleteBackground(offsetX.value)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -dismissThreshold) {
                                    offsetX.animateTo(-1000f, tween(300))
                                    delay(200)
                                    onDelete(item)
                                } else {
                                    offsetX.animateTo(0f, tween(200))
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                    }
                }
                .offset { IntOffset(offsetX.value.toInt(), 0) }
        ) {
            content(item)
        }
    }
}


@Composable
fun DeleteBackground(offsetX: Float) {
    val backgroundColor by animateColorAsState(
        targetValue = if (offsetX < -100f) Color.Red else Color.Transparent,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (offsetX < -100f) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Icon",
                tint = Color.White
            )
        }
    }
}

