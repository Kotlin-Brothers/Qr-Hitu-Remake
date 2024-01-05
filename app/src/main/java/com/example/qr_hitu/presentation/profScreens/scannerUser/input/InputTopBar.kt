package com.example.qr_hitu.presentation.profScreens.scannerUser.input

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.qr_hitu.components.UserChoices
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.presentation.ui.MainTopBar
import com.example.qr_hitu.presentation.ui.MenuOptions
import com.example.qr_hitu.presentation.ui.emailString


@Composable
fun TopBarInput(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    MainTopBar(
        title = { email ?: "" },
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {
            IconButton(onClick = { navController.navigate(UserChoices.route) }) {
                Icon(
                    Icons.Filled.Close,
                    "Close",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}