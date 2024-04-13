@file:Suppress("UNUSED_PARAMETER")

package com.example.qr_hitu.presentation.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.functions.SettingsManager


//  Tela de loading enquanto faz log in
@Composable
fun LoadingScreen(navController: NavController, settingsManager: SettingsManager, isDarkTheme: Boolean = isSystemInDarkTheme()){

    // State with the progress of the animation
    val animationProgress = remember { Animatable(-0.5f) }
    // Checks for the theme to define which image to use in loading
    val switch = remember { mutableStateOf("") }
    val theme by rememberUpdatedState(
        if (switch.value == "") {
            settingsManager.getSetting("Theme", "")
        } else switch.value
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Opens Coroutine
        LaunchedEffect(Unit) {
            // The animation only works while on the screen
            while (true) {
                animationProgress.animateTo(
                    targetValue = 0.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1500),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                animationProgress.snapTo(0f)
            }
        }

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 5.dp
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Text(text = stringResource(R.string.loading), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondary)
    }
}
