package com.example.qr_hitu.presentation.profScreens.scannerUser.input

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.qr_hitu.components.UserChoices
import com.example.qr_hitu.presentation.ui.overlays.MenuOptions
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.presentation.ui.overlays.emailString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarInput(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
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