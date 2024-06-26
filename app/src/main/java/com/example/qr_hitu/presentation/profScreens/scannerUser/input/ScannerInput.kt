package com.example.qr_hitu.presentation.profScreens.scannerUser.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.components.UserChoices
import com.example.qr_hitu.functions.SendEmail
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.addMalfunction
import com.example.qr_hitu.functions.getOptions
import com.example.qr_hitu.functions.malfunctionExists
import com.example.qr_hitu.presentation.ui.DError_Success_Dialogs
import com.example.qr_hitu.presentation.ui.WarningDialog
import com.example.qr_hitu.presentation.viewModels.ScannerViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


//  Tela para escolher qual avaria o dispositivo tem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerInput(
    navController: NavController,
    viewModel: ScannerViewModel,
    settingsManager: SettingsManager
) {

    val dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val formattedDateTime = dateTime.format(formatter);
    //  Variável que controla se tem de enviar email
    val sendE = remember { mutableStateOf(false) }
    //  Email do utilizador
    val email = Firebase.auth.currentUser?.email.toString()
    //  Guarda as informações escolhidas pelo utilizador
    var outro by remember { mutableStateOf("") }
    var malfunction by remember { mutableStateOf("") }
    var urgentState by remember { mutableStateOf(false) }
    //  Mostrar Dialogs
    val show = remember { mutableStateOf(false) }
    val show1 = remember { mutableStateOf(false) }
    val err = remember { mutableStateOf(false) }
    //  Opções de avaria
    var combinedOptions by remember { mutableStateOf(listOf<String>()) }
    //  Chama função para conseguir as opções
    getOptions { optionsFromFirestore ->
        combinedOptions = optionsFromFirestore + "Outro"
    }
    //  Dados da localização do dispositivo
    val (block, room, machine) = viewModel.myData.value!!.split(",")

    //  Estados para ativar os componentes
    var enabled2 by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(false) }
    //  Condição para ativar os componentes
    enabled = malfunction == "Outro"
    enabled2 = malfunction.isNotEmpty() || malfunction == "Outro" && outro.isNotEmpty()

    var textFiledSize by remember { mutableStateOf(Size.Zero) }
    //  Estado para ver se a dropbox está expandida ou não
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    //  Condição para trocar o icon dependo se a dropbox está expandida ou não
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Scaffold(
        topBar = { TopBarInput(navController, settingsManager) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.padding(20.dp))

                Text(
                    text = stringResource(R.string.problem),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.padding(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = malfunction,
                        readOnly = true,
                        onValueChange = { malfunction = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .onGloballyPositioned { coordinates ->
                                textFiledSize = coordinates.size.toSize()
                            },
                        label = { Text(text = stringResource(R.string.problemChoice)) },
                        trailingIcon = {
                            Icon(icon, "", Modifier.clickable { expanded = !expanded })
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        combinedOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = { expanded = false; malfunction = option },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = stringResource(R.string.problemDesc),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    value = outro,
                    onValueChange = { outro = it },
                    enabled = enabled,
                    label = { Text("Outro") },
                    placeholder = { Text("Outro") },
                    singleLine = false,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = urgentState,
                        onClick = {
                            urgentState = !urgentState
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = stringResource(R.string.urgent),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        //  Verifica se já existe alerta de avaria
                        malfunctionExists(room, machine) { exists ->
                            //  Caso não exista faz as verificações para adicionar a avaria
                            if (!exists) {
                                when (malfunction) {
                                    "Outro" -> {
                                        //  Caso a opção escolhida seja "Outro" e estaja vazia mostra Dialog de erro
                                        when (outro) {
                                            "" -> {
                                                show.value = true
                                                err.value = true
                                            }
                                            //  Se não, adiciona avaria e envia email
                                            else -> {
                                                show.value = true
                                                sendE.value = true
                                                addMalfunction(
                                                    block,
                                                    room,
                                                    machine,
                                                    outro,
                                                    urgentState,
                                                    email,
                                                    formattedDateTime.toString()
                                                )
                                            }
                                        }
                                    }
                                    //  Caso não, adiciona avaria e envia email
                                    else -> {
                                        show.value = true
                                        sendE.value = true
                                        addMalfunction(
                                            block,
                                            room,
                                            machine,
                                            malfunction,
                                            urgentState,
                                            email,
                                            formattedDateTime.toString()
                                        )
                                    }
                                }
                                //  Caso já exista mostra Dialog de erro
                            } else {
                                show1.value = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    enabled = enabled2
                ) {
                    Text(
                        stringResource(R.string.problemSend),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                //  Condição para enviar email
                if (sendE.value) {
                    //  Verifica qual email mandar
                    when (malfunction) {
                        "Outro" -> SendEmail(
                            email,
                            block,
                            room,
                            machine,
                            "uma avaria",
                            "Avaria",
                            outro,
                            urgentState
                        )

                        else -> SendEmail(
                            email,
                            block,
                            room,
                            machine,
                            "uma avaria",
                            "Avaria",
                            malfunction,
                            urgentState
                        )
                    }
                    sendE.value = false
                }

                //  Condição para saber qual Dialog mostrar
                when {
                    show.value -> DError_Success_Dialogs(
                        error = err.value,
                        onDialogDismiss = {
                            show.value = false; navController.navigate(UserChoices.route)
                        },
                        onDialogDismissError = { show.value = false; err.value = false; }
                    )

                    show1.value -> WarningDialog(
                        onDialogDismiss = {
                            show1.value = false; navController.navigate(UserChoices.route)
                        },
                        title = stringResource(R.string.existWMDtitle),
                        text = stringResource(R.string.existWMDtext)
                    )
                }
            }
        }
    }
}