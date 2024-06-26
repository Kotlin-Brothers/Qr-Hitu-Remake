package com.example.qr_hitu.presentation.adminScreens.create.phasefinal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.presentation.viewModels.ViewModel1
import com.example.qr_hitu.presentation.viewModels.ViewModel2
import com.example.qr_hitu.components.AdminChoices
import com.example.qr_hitu.functions.createQR
import com.example.qr_hitu.functions.snackbar
import com.example.qr_hitu.functions.addDispositivo
import com.example.qr_hitu.functions.downloadQR
import com.example.qr_hitu.functions.encryptAES
import com.example.qr_hitu.functions.encryptionKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//  Tela final para criar QR Code
@Composable
fun QrCreateFinal(navController: NavController, viewModel1: ViewModel1, viewModel2: ViewModel2) {

    //  Conteúdo e nome do QR Code
    var content by remember { mutableStateOf("") }
    var qrName by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    //  Coroutine
    val scope = rememberCoroutineScope()
    //  Mostrar SnackBars
    val showErr = remember { mutableStateOf(false) }
    val showDone = remember { mutableStateOf(false) }
    //  Verifica se a pessoa já escreveu um nome para o ficheiro
    val enabled = qrName.isNotEmpty()

    //  Dados vindos das telas CreateQrPhase 1 e 2
    val myData = viewModel1.myData.value
    val myData2 = viewModel2.myData.value
    //  Map das informações para guardar na db
    val spec = hashMapOf(
        "Nome" to "${myData2?.name}",
        "Processador" to "${myData2?.processor}",
        "Ram" to "${myData2?.ram}",
        "Fonte" to "${myData2?.powerSupply}"
    )


    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Spacer(modifier = Modifier.padding(60.dp))

            //  Verifica se a data está vazia antes de adicionar á base de dados
            LaunchedEffect(myData){
                if (myData != null) {
                    content = encryptAES("${myData.block},${myData.room},${myData.machine}", encryptionKey)
                    addDispositivo(myData.block, myData.room, myData.machine, spec)
                }
            }

            //  Cria o bitmap
            val bitmap = createQR(content = content)
            Image(bitmap = bitmap, contentDescription = "qr", Modifier.size(200.dp))

            Spacer(modifier = Modifier.height(20.dp))

            //  TextField para escrever o nome do ficheiro
            OutlinedTextField(
                value = qrName,
                onValueChange = {
                    qrName = it
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                label = { Text(text = stringResource(R.string.createNamePlaceholder)) },
                placeholder = { Text(text = stringResource(R.string.createName)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  Boão para fazer download, apenas está ativo se o utilizador já tiver escrito um nome
            Button(
                onClick = {
                    //  Abre coroutine
                    scope.launch {
                        try {
                            //  Faz download do QR Code e mostra a snackbar de sucesso
                            downloadQR(bitmap, qrName)
                            showDone.value = true
                        } catch (e: Exception) {
                            //  Caso falhe mostra snackbar de erro
                            showErr.value = true
                        }
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                enabled = enabled,
            ) {
                Text(
                    stringResource(R.string.createDownload),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        //  Condição para chamar a snackbar de sucesso ou de erro
        when {
            showDone.value -> {
                Box(contentAlignment = Alignment.BottomCenter) {
                    snackbar(text = stringResource(R.string.downloadStext))
                    //  Abre coroutine
                    LaunchedEffect(Unit) {
                        //  Dá um delay de 2 segundos antes de fechar a snackbar e enviar o utilizador para a tela de escolha inicial
                        delay(2000)
                        showDone.value = false
                        navController.navigate(AdminChoices.route)
                    }
                }
            }

            showErr.value -> {
                Box(contentAlignment = Alignment.BottomCenter) {
                    snackbar(text = stringResource(R.string.downloadSEtext))
                    //  Abre coroutine
                    LaunchedEffect(Unit) {
                        //  Dá delay de 2 segundos antes de fechar a snackbar de erro
                        delay(2000)
                        showErr.value = false
                    }
                }
            }
        }
    }
}