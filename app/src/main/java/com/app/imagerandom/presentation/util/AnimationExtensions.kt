package com.app.imagerandom.presentation.util

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
fun Transition<Boolean>.animateFloatValue(
    duration: Int = 300,
    targetValue: (Boolean) -> Float,
) = animateFloat(
    transitionSpec = { tween(durationMillis = duration) },
    label = "FloatAnim"
) { state -> targetValue(state) }

@Composable
fun Transition<Boolean>.animateDpValue(
    duration: Int = 300,
    easing: Easing = FastOutSlowInEasing,
    targetValue: (Boolean) -> Dp,
) = animateDp(
    transitionSpec = { tween(durationMillis = duration, easing = easing) },
    label = "DpAnim"
) { state -> targetValue(state) }