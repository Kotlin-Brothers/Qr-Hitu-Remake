package com.example.qr_hitu.presentation.adminScreens.create.phase2

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.qr_hitu.presentation.ui.overlays.emailString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPhase2(navController: NavController){
    val email = emailString()

    TopAppBar(
        title = { Text(text = email, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack ,"Back", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    )

}
