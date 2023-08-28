package com.example.qr_hitu.presentation.adminScreens.scannerAdm.info

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.components.ScannerAdminInfoUpdate
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.functions.DelDialog
import com.example.qr_hitu.ui.overlays.MenuOptions
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.delDispositivo
import com.example.qr_hitu.ui.overlays.emailString
import com.example.qr_hitu.presentation.viewModels.ScannerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarInfo(navController: NavController, viewModel: ScannerViewModel, settingsManager: SettingsManager){
    val email = emailString()

    val showState = remember { mutableStateOf(false) }
    val show by rememberUpdatedState(showState.value)
    val (block, room, machine) = viewModel.myData.value.toString().split(",")

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            MenuOptions(navController = navController, settingsManager = settingsManager)
        },
        actions = {

            IconButton(onClick = { navController.navigate(ScannerAdminInfoUpdate.route) }) {
                Icon(Icons.Filled.Edit, "Edit", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            IconButton(onClick = { showState.value = true }) {
                Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
    )
    if(show){
        DelDialog(
            onDialogDismissed = { showState.value = false},
            onDeleteClick = { delDispositivo(block, room, machine); navController.navigate(TabScreen.route) },
            title = stringResource(R.string.delDtitle),
            text =  stringResource(R.string.delDtext)
        )
    }
}