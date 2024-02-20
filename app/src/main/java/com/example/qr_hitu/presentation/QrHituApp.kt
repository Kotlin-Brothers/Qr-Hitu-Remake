package com.example.qr_hitu.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.setLocale
import com.example.qr_hitu.presentation.theme.QRHITUTheme
import com.example.qr_hitu.presentation.ui.ScaffoldLayouts


//  Neste ficheiro é onde toda a aplicação se liga e todas as partes importantes são definidas
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun QrHituApp() {
    val localContext = LocalContext.current
    //  Inicialização do navegador
    val navController = rememberNavController()
    //  Inicialização das settings
    val settingsManager = SettingsManager(localContext)
    //  Inicialização dos viewModels
    //  Estado de troca de tema
    val switch = remember { mutableStateOf("") }
    //  Verificação de qual idioma está selecionado
    val theme by rememberUpdatedState(
        if (switch.value == "") {
            settingsManager.getSetting("Theme", "")
        } else switch.value
    )
    setLocale(
        if (settingsManager.getSetting("Language", "pt") == "pt") "pt" else "en",
        localContext
    )

    //  Tema da app
    QRHITUTheme(
        content = {
            //  Resto da app
            ScaffoldLayouts(navController, settingsManager, switch)
        },
        theme = theme
    )

}
