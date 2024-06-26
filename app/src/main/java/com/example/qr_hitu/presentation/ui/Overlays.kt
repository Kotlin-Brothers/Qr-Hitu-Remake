package com.example.qr_hitu.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.qr_hitu.R
import com.example.qr_hitu.components.About
import com.example.qr_hitu.components.AdminChoices
import com.example.qr_hitu.components.ChooseQr
import com.example.qr_hitu.components.Create1
import com.example.qr_hitu.components.Create2
import com.example.qr_hitu.components.Create3
import com.example.qr_hitu.components.Feedback
import com.example.qr_hitu.components.ForgotPass
import com.example.qr_hitu.components.Login
import com.example.qr_hitu.components.MQRLocal
import com.example.qr_hitu.components.MalfInfo
import com.example.qr_hitu.components.Manual
import com.example.qr_hitu.components.QrHituNavHost
import com.example.qr_hitu.components.ScanAdmin
import com.example.qr_hitu.components.ScanProf
import com.example.qr_hitu.components.ScannerAdminInfo
import com.example.qr_hitu.components.ScannerAdminInfoUpdate
import com.example.qr_hitu.components.SettingOptions
import com.example.qr_hitu.components.TabScreen
import com.example.qr_hitu.components.TransferQr
import com.example.qr_hitu.components.UserChoices
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.presentation.adminScreens.scannerAdm.info.TopBarInfo
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

                destinationRoute.contains(ScannerAdminInfo.route) -> TopBarInfo(
                    navController,
                    settingsManager
                )

                destinationRoute.contains(Create1.route) || destinationRoute.contains(ChooseQr.route) ->
                    TopBarBackAdmin(
                        navController = navController,
                        settingsManager = settingsManager
                    )

                destinationRoute.contains(Create3.route) || destinationRoute.contains(TransferQr.route) ->
                    TopBarExitAdmin(navController = navController)

                listOf(
                    SettingOptions.route,
                    Manual.route,
                    MalfInfo.route,
                    ForgotPass.route,
                    About.route,
                    Feedback.route
                )
                    .any { it in destinationRoute } ->
                    TopBarEmpty(navController = navController)

                listOf(
                    ScanAdmin.route,
                    AdminChoices.route,
                    TabScreen.route,
                    ScannerAdminInfoUpdate.route,
                    UserChoices.route
                )
                    .any { it in destinationRoute } ->
                    TopBarUni(navController = navController, settingsManager)

                listOf(ScanProf.route, MQRLocal.route, Create2.route)
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
                    text = stringResource(R.string.feedback),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            onClick = { navController.navigate(Feedback.route) },
            leadingIcon = {
                Icon(Icons.Filled.Feedback, "Feedback")
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
fun emailString(): String? {

    val user = Firebase.auth.currentUser ?: return null
    val email = user.email ?: return null

    val formattedEmail = email.split(".")[0].split("@")[0].replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    return formattedEmail
}
