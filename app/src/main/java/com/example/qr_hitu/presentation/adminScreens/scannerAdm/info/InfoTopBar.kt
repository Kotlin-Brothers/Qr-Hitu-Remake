package com.example.qr_hitu.presentation.adminScreens.scannerAdm.info

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.components.ScannerAdminInfoUpdate
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.delDispositivo
import com.example.qr_hitu.presentation.ui.DelDialog
import com.example.qr_hitu.presentation.ui.MainTopBar
import com.example.qr_hitu.presentation.ui.MenuOptions
import com.example.qr_hitu.presentation.ui.emailString
import com.example.qr_hitu.presentation.viewModels.ScannerViewModel

@Composable
fun TopBarInfo(navController: NavController, settingsManager: SettingsManager) {
    val email = emailString()
    val viewModel: ScannerViewModel = hiltViewModel()
    val showState = remember { mutableStateOf(false) }
    val show by rememberUpdatedState(showState.value)
    val (block, room, machine) = viewModel.myData.value.toString().split(",")

    MainTopBar(
        title = { email ?: "" },
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {

            IconButton(onClick = { navController.navigate(ScannerAdminInfoUpdate.route) }) {
                Icon(Icons.Filled.Edit, "Edit", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            IconButton(onClick = { showState.value = true }) {
                Icon(
                    Icons.Filled.Delete,
                    "Delete",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
    )
    if (show) {
        DelDialog(
            onDialogDismiss = { showState.value = false },
            onDeleteClick = {
                delDispositivo(
                    block,
                    room,
                    machine
                ); navController.navigate(TabScreen.route)
            },
            title = stringResource(R.string.delDtitle),
            text = stringResource(R.string.delDtext)
        )
    }
}