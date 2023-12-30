package com.example.qr_hitu.presentation.ui.overlays

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.functions.SettingsManager


@Composable
fun TopBarEmpty(navController: NavController) {
    MainTopBar(
        title = { "" },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        actions = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBackUser(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    MainTopBar(
        title = { email ?: "" },
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBackAdmin(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    MainTopBar(
        title = { email ?: "" },
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@Composable
fun TopBarExitAdmin(navController: NavController) {
    val email = emailString()

    MainTopBar(
        title = { email ?: "" },
        actions = {
            IconButton(onClick = { navController.navigate(TabScreen.route) }) {
                Icon(
                    Icons.Filled.Close,
                    "Close",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUni(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    MainTopBar(
        title = { email ?: "" },
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {}
    )
}