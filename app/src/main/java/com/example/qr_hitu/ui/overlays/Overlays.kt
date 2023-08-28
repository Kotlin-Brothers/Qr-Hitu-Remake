package com.example.qr_hitu.ui.overlays

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Locale

//  Neste ficheiro está a Scaffold que engloba a aplicação toda e também a bottom bar e todas as topbars


//  Função com a Scaffold da app
@Composable
fun ScaffoldLayouts(
    navController: NavHostController,
    settingsManager: SettingsManager,
    viewModel1: ViewModel1,
    viewModel2: ViewModel2,
    viewModelSA: ScannerViewModel,
    viewModelMF: MalfunctionViewModel,
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
                destinationRoute.contains(Create1.route) || destinationRoute.contains(ChooseQr.route) -> TopBarBackAdmin(navController = navController, settingsManager = settingsManager)

                destinationRoute.contains(Create3.route) || destinationRoute.contains(TransferQr.route) -> TopBarExitAdmin(navController = navController)

                destinationRoute.contains(SettingOptions.route) || destinationRoute.contains(Manual.route) || destinationRoute.contains(MalfInfo.route) || destinationRoute.contains(ForgotPass.route)
                        || destinationRoute.contains(About.route) -> TopBarEmpty(navController = navController)

                destinationRoute.contains(ScanAdmin.route) || destinationRoute.contains(AdminChoices.route) || destinationRoute.contains(TabScreen.route)
                        || destinationRoute.contains(ScannerAdminInfoUpdate.route) -> TopBarUniAdmin(navController = navController, settingsManager)

                destinationRoute.contains(ScanProf.route) || destinationRoute.contains(MQRLocal.route) -> TopBarBackUser(navController = navController, settingsManager)
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
            viewModel1 = viewModel1,
            viewModel2 = viewModel2,
            viewModelSA = viewModelSA,
            viewModelMF = viewModelMF,
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
                    Firebase.auth.signOut()
                    settingsManager.saveSetting("Admin", "User")
                    navController.navigate(Login.route)
                }
            },
            leadingIcon = {
                Icon(Icons.Filled.Logout, "Logout")
            }
        )
    }
}


//  Função para retirar o nome do utilizador apartir do email (isto contando que o email está no formato x.y@outlook.pt)
fun emailString(): String {
    //  Pega no email do utilizador separa e torna a primeira letra em maiúscula
    val email = Firebase.auth.currentUser?.email!!.split(".")[0].split("@")[0].replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
    //  Retorna a string
    return email
}