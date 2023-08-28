package com.example.qr_hitu.ui.overlays

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.functions.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarEmpty(navController: NavController) {

    TopAppBar(
        title = { Text(text = "", color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
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
fun TopBarBackUser(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
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

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
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
fun TopBarExitAdmin(navController: NavController) {
    val email = emailString()

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            IconButton(onClick = { navController.navigate(TabScreen.route) }) {
                Icon(
                    Icons.Filled.Close,
                    "Close",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUniAdmin(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        }
    )
}