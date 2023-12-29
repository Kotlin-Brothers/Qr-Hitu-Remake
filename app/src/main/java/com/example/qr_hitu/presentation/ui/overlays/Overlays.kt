package com.example.qr_hitu.presentation.ui.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.qr_hitu.R
import com.example.qr_hitu.presentation.viewModels.MalfunctionViewModel
import com.example.qr_hitu.presentation.viewModels.ScannerViewModel
import com.example.qr_hitu.presentation.viewModels.ViewModel1
import com.example.qr_hitu.presentation.viewModels.ViewModel2
import com.example.qr_hitu.components.*
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.presentation.adminScreens.scannerAdm.info.TopBarInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Scanner

//  Neste ficheiro está a Scaffold que engloba a aplicação toda e também a bottom bar e todas as topbars


//  Função com a Scaffold da app
@Composable
fun ScaffoldLayouts(
    navController: NavHostController,
    settingsManager: SettingsManager,
    switch: MutableState<String>
) {

    // navegação
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val destinationRoute = currentDestination?.route ?: Login.route

    Scaffold(
        topBar = {
            //  Condição que verifica em que ecrã estamos e chama a topbar indicada

            when {

                destinationRoute.contains(ScannerAdminInfo.route) -> TopBarInfo(navController, settingsManager)

                destinationRoute.contains(Create1.route) || destinationRoute.contains(ChooseQr.route) ->
                    TopBarBackAdmin(navController = navController, settingsManager = settingsManager)

                destinationRoute.contains(Create3.route) || destinationRoute.contains(TransferQr.route) ->
                    TopBarExitAdmin(navController = navController)

                listOf(SettingOptions.route, Manual.route, MalfInfo.route, ForgotPass.route, About.route)
                    .any { it in destinationRoute } ->
                    TopBarEmpty(navController = navController)

                listOf(ScanAdmin.route, AdminChoices.route, TabScreen.route, ScannerAdminInfoUpdate.route)
                    .any { it in destinationRoute } ->
                    TopBarUniAdmin(navController = navController, settingsManager)

                listOf(ScanProf.route, MQRLocal.route)
                    .any { it in destinationRoute } ->
                    TopBarBackUser(navController = navController, settingsManager = settingsManager)

            }

        },
        bottomBar = {
            //  Condição para apenas chamar a bottombar quando necessário
            when {
                destinationRoute.contains(TabScreen.route) || destinationRoute.contains(ScanAdmin.route) || destinationRoute.contains(
                    AdminChoices.route
                ) -> BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        //  Navegador da aplicação
        QrHituNavHost(
            navController = navController,
            settingsManager = settingsManager,
            switch = switch,
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == TabScreen.route,
            label = {
                Text(
                    text = stringResource(R.string.list),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = { navController.navigate(TabScreen.route) },
            icon = {
                Icon(Icons.Filled.FormatListBulleted, "Listas")
            }
        )
        NavigationBarItem(
            selected =
            navController.currentBackStackEntry?.destination?.route == ScanAdmin.route ||
                    navController.currentBackStackEntry?.destination?.route == ScannerAdminInfo.route ||
                    navController.currentBackStackEntry?.destination?.route == ScannerAdminInfoUpdate.route,
            label = {
                Text(
                    text = stringResource(R.string.scanner),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = { navController.navigate(ScanAdmin.route) },
            icon = {
                Icon(Icons.Filled.QrCodeScanner, "Scanner")
            }
        )
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == AdminChoices.route,
            label = {
                Text(
                    text = stringResource(R.string.create),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = { navController.navigate(AdminChoices.route) },
            icon = {
                Icon(Icons.Filled.QrCode2, "Create")
            }
        )
    }
}

@Composable
fun MenuOptions(navController: NavController, settingsManager: SettingsManager) {

    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(Icons.Filled.Menu, "Menu", tint = MaterialTheme.colorScheme.onPrimaryContainer)
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            onClick = { navController.navigate(SettingOptions.route) },
            leadingIcon = {
                Icon(Icons.Filled.Settings, "Settings")
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.manual),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            onClick = { navController.navigate(Manual.route) },
            leadingIcon = {
                Icon(Icons.Filled.Book, "Manual")
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            onClick = {
                scope.launch {
                    try {
                        Firebase.auth.signOut()
                        settingsManager.saveSetting("Admin", "User")
                        navController.navigate(Login.route)
                    } catch (e: Exception) {
                        // Handle the exception
                        println(e.message)
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Filled.Logout, "Logout")
            }
        )
    }
}


//  Função para retirar o nome do utilizador apartir do email (isto contando que o email está no formato x.y@outlook.pt)
fun emailString(): String? {

    val user = Firebase.auth.currentUser ?: return null
    val email = user.email ?: return null

    val formattedEmail = email.split(".")[0].split("@")[0].replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    return formattedEmail
}
