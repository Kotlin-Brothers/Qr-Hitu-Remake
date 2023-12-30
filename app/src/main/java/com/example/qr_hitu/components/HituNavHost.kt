package com.example.qr_hitu.components


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qr_hitu.presentation.viewModels.MalfunctionViewModel
import com.example.qr_hitu.presentation.viewModels.ScannerViewModel
import com.example.qr_hitu.presentation.viewModels.ViewModel1
import com.example.qr_hitu.presentation.viewModels.ViewModel2
import com.example.qr_hitu.functions.ConnectionState
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.WifiWarn
import com.example.qr_hitu.functions.connectivityState
import com.example.qr_hitu.presentation.loading.LoadingScreen
import com.example.qr_hitu.presentation.adminScreens.adminChoices.AdminChoices
import com.example.qr_hitu.presentation.adminScreens.create.phasefinal.QrCreateFinal
import com.example.qr_hitu.presentation.adminScreens.create.phase1.QrCreatePhase1
import com.example.qr_hitu.presentation.adminScreens.create.phase2.QrCreatePhase2
import com.example.qr_hitu.presentation.adminScreens.scannerAdm.info.ScannerAdminInfo
import com.example.qr_hitu.presentation.adminScreens.scannerAdm.update.ScannerAdminInfoUpdate
import com.example.qr_hitu.presentation.adminScreens.scannerAdm.scanner.ScannerAdminScreen
import com.example.qr_hitu.presentation.adminScreens.tabLists.TabLayout
import com.example.qr_hitu.presentation.adminScreens.tabLists.malfunctionsList.MalfInfo
import com.example.qr_hitu.presentation.adminScreens.tabLists.malfunctionsList.MalfList
import com.example.qr_hitu.presentation.adminScreens.tabLists.missingQrList.MissingQrList
import com.example.qr_hitu.presentation.adminScreens.tabLists.recentScanList.RecentScanList
import com.example.qr_hitu.presentation.adminScreens.transfer.ChooseQr
import com.example.qr_hitu.presentation.adminScreens.transfer.TransferQr
import com.example.qr_hitu.presentation.login.ForgotPass
import com.example.qr_hitu.presentation.login.LoginScreen
import com.example.qr_hitu.presentation.menu.About
import com.example.qr_hitu.presentation.menu.Feedback
import com.example.qr_hitu.presentation.menu.Manual
import com.example.qr_hitu.presentation.menu.SettingsOptions
import com.example.qr_hitu.presentation.profScreens.missingQr.MQRLocal
import com.example.qr_hitu.presentation.profScreens.userChoices.PrimaryChoice
import com.example.qr_hitu.presentation.profScreens.scannerUser.input.ScannerInput
import com.example.qr_hitu.presentation.profScreens.scannerUser.scanner.ScannerTeachScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

//Função de navegação da app
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QrHituNavHost(
    navController: NavHostController = rememberNavController(),
    settingsManager: SettingsManager,
    startDestination: String = Login.route,
    switch: MutableState<String>,
    modifier: Modifier
) {
    //Variáveis usadas para o controlo de conexão á internet da app
    val netstate by connectivityState()
    val isConnected = netstate === ConnectionState.Available

    //Inicio da UI
    //Coluna que contém o navegador ou NavHost
    Column(modifier = modifier) {

        val viewModel1: ViewModel1 = hiltViewModel()
        val viewModel2: ViewModel2 = hiltViewModel()
        val viewModelSA: ScannerViewModel = hiltViewModel()
        val viewModelMF: MalfunctionViewModel = hiltViewModel()

        //NavHost é oq controla a navegação da app, nesta função contém todas as telas com a respetiva função para cada
        //É aqui que é ligado os objetos definidos na ficheiro HituDestination a cada ecrã
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Login.route) {
                LoginScreen(navController, settingsManager)
            }
            composable(ScanProf.route) {
                ScannerTeachScreen(navController, viewModelSA)
            }
            composable(ScanInput.route) {
                ScannerInput(navController, viewModelSA, settingsManager)
            }
            composable(MalfList.route) {
                MalfList(navController, viewModelMF)
            }
            composable(MalfInfo.route) {
                MalfInfo(navController, viewModelMF)
            }
            composable(Create1.route) {
                QrCreatePhase1(navController, viewModel1)
            }
            composable(Create2.route) {
                QrCreatePhase2(navController, viewModel2)
            }
            composable(Create3.route) {
                QrCreateFinal(navController, viewModel1, viewModel2)
            }
            composable(Create3.route) {
                QrCreateFinal(navController, viewModel1, viewModel2)
            }
            composable(ChooseQr.route) {
                ChooseQr(navController, viewModel1)
            }
            composable(TransferQr.route) {
                TransferQr(navController, viewModel1)
            }
            composable(AdminChoices.route) {
                AdminChoices(navController)
            }
            composable(ScanAdmin.route) {
                ScannerAdminScreen(navController, viewModelSA, settingsManager)
            }
            composable(ScannerAdminInfo.route) {
                ScannerAdminInfo( viewModelSA )
            }
            composable(ScannerAdminInfoUpdate.route) {
                ScannerAdminInfoUpdate(navController, viewModelSA)
            }
            composable(SettingOptions.route) {
                SettingsOptions(navController, settingsManager, switch)
            }
            composable(Manual.route) {
                Manual(navController, settingsManager)
            }
            composable(Loading.route) {
                LoadingScreen(navController, settingsManager)
            }
            composable(UserChoices.route) {
                PrimaryChoice(navController, settingsManager)
            }
            composable(MQRLocal.route) {
                MQRLocal(navController, viewModelSA)
            }
            composable(RecentScanList.route) {
                RecentScanList(navController, settingsManager, viewModelSA)
            }
            composable(MissingQrList.route) {
                MissingQrList(navController)
            }
            composable(TabScreen.route) {
                TabLayout(navController, settingsManager, viewModelSA, viewModelMF)
            }
            composable(ForgotPass.route) {
                ForgotPass(navController)
            }
            composable(About.route) {
                About(navController)
            }
            composable(WifiWarn.route) {
                WifiWarn(navController)
            }
            composable(Feedback.route){
                Feedback(navController)
            }
        }
    }

    //Condição que verifica se a varíavel isConnected == fales
    if (!isConnected) {
        //Para evitar repetição temos esta condição que verifica se o utilizador já está ou não na tela de erro por falta de internet
        if (navController.currentDestination!!.route != WifiWarn.route) {
            //Navegação
            navController.navigate(WifiWarn.route)
        }
    }
    //Condição para que assim que o utilizador voltar a ter internet e ainda esteja na tela de erro voltar para trás
    if (navController.currentDestination!!.route == WifiWarn.route) {
        if (isConnected) {
            //Navegação
            navController.popBackStack()
        }
    }

}