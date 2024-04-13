package com.example.qr_hitu.presentation.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.components.ScanInput
import com.example.qr_hitu.functions.SettingsManager
import com.example.qr_hitu.functions.setLocale

//  Create QR Phase 1
@Composable
fun ExistsInvDialog(onDialogConfirm: () -> Unit, onDialogDismiss: () -> Unit) {
    MainDialog(
        title = { stringResource(R.string.existDtitle) },
        bodyText = {
            Text(
                text = stringResource(R.string.existDtext),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = { onDialogConfirm(); }) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDialogDismiss(); }) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        onDialogDismiss = { onDialogDismiss() },
    )
}

//  Missing Qr, User Scanner Input and Admin Scanner
@Composable
fun WarningDialog(onDialogDismiss: () -> Unit, title: String, text: String) {
    MainDialog(
        title = { title },
        bodyText = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = { onDialogDismiss() }) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = { },
        onDialogDismiss = { onDialogDismiss() },
    )
}

//  User Scanner Input
@Composable
fun AddMalfDialog(onDialogDismiss: () -> Unit, onDialogConfirm: () -> Unit) {
    MainDialog(
        title = { stringResource(R.string.addMalfDtitle) },
        bodyText = {
            Text(
                text = stringResource(R.string.addMalfDtext),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = { onDialogConfirm() }) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDialogDismiss() }) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        onDialogDismiss = { onDialogDismiss() })
}

//  User Scanner
@Composable
fun Malf_ErrorDialogs(onDialogDismiss: () -> Unit, navController: NavController, Err: Boolean) {
    //  Condição para verificar se houve um erro
    if (!Err) {
        MainDialog(
            title = { stringResource(R.string.addMalfDtitle) },
            bodyText = {
                Text(
                    text = stringResource(R.string.addMalfDtext),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { navController.navigate(ScanInput.route) }) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onDialogDismiss() }) {
                    Text(
                        text = stringResource(R.string.dismiss),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            onDialogDismiss = { onDialogDismiss() })
    } else {
        MainDialog(
            title = { stringResource(R.string.error) },
            bodyText = {
                Text(
                    text = stringResource(R.string.invalidDtext),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { onDialogDismiss() }) {
                    Text(text = "OK")
                }
            },
            dismissButton = { },
            onDialogDismiss = { onDialogDismiss() })
    }
}

//  User Scanner Input
@Composable
fun DError_Success_Dialogs(
    error: Boolean,
    onDialogDismissError: () -> Unit,
    onDialogDismiss: () -> Unit
) {
    //  Condição para verificar se houve um erro
    if (error) {
        MainDialog(
            title = { stringResource(R.string.error) },
            bodyText = {
                Text(
                    text = stringResource(R.string.descMalftext),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { onDialogDismissError() }) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            dismissButton = { },
            onDialogDismiss = { onDialogDismissError() })
    } else {
        MainDialog(
            title = { stringResource(R.string.success) },
            bodyText = {
                Text(
                    text = stringResource(R.string.descMalftext2),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { onDialogDismiss() }) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            dismissButton = { },
            onDialogDismiss = { onDialogDismiss() })
    }
}

//  Settings
@Composable
fun ThemeDialog(
    showThemeState: MutableState<Boolean>,
    selectedTheme: MutableState<String>,
    switch: MutableState<String>,
    settingsManager: SettingsManager
) {
    MainDialog(
        title = { stringResource(R.string.themeDTitle) },
        bodyText = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedTheme.value == "Light",
                        onClick = {
                            //  Logística para trocar para tema claro
                            selectedTheme.value = "Light"
                            settingsManager.saveSetting("Theme", "Light")
                            switch.value = "Light"
                        }
                    )
                    Text(stringResource(R.string.themeL))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedTheme.value == "Dark",
                        onClick = {
                            //  Logística para trocar para tema escuro
                            selectedTheme.value = "Dark"
                            settingsManager.saveSetting("Theme", "Dark")
                            switch.value = "Dark"
                        }
                    )
                    Text(stringResource(R.string.themeD))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { showThemeState.value = false }) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = { },
        onDialogDismiss = { showThemeState.value = false })
}

//  Settings
@Composable
fun LanguageDialog(
    showLanguageState: MutableState<Boolean>,
    languageSelect: MutableState<String>,
    settingsManager: SettingsManager,
    context: Context,
    setRecompose: (Boolean) -> Unit
) {
    MainDialog(
        title = { stringResource(R.string.langDTitle) },
        bodyText = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = languageSelect.value == "pt",
                        onClick = {
                            //  Logística para trocar para português
                            languageSelect.value = "pt"
                            settingsManager.saveSetting("Language", "pt")
                            setLocale("pt", context)
                            //  Ativar recompose
                            setRecompose(true)
                            //  Fechar Dialog
                            showLanguageState.value = false
                        }
                    )
                    Text(stringResource(R.string.langPT))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = languageSelect.value == "en",
                        onClick = {
                            //  Logística para trocar para inglês
                            languageSelect.value = "en"
                            settingsManager.saveSetting("Language", "en")
                            setLocale("en", context)
                            //  Ativar recompose
                            setRecompose(true)
                            //  Fechar Dialog
                            showLanguageState.value = false
                        }
                    )
                    Text(stringResource(R.string.langEN))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { showLanguageState.value = false }) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = { },
        onDialogDismiss = { showLanguageState.value = false })
}

//  Overlays and Missing Qr List
@Composable
fun DelDialog(
    onDialogDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
    title: String,
    text: String
) {
    MainDialog(
        title = { title },
        bodyText = { Text(text = text, style = MaterialTheme.typography.bodyLarge) },
        confirmButton = {
            TextButton(onClick = { onDialogDismiss(); onDeleteClick() }) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDialogDismiss() }) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        onDialogDismiss = { onDialogDismiss() })
}


//  Malfunction Info
@Composable
fun CompleteMalfDialog(onDialogDismiss: () -> Unit, onDialogConfirm: () -> Unit) {
    MainDialog(
        title = { stringResource(R.string.compMDtitle) },
        bodyText = {
            Text(
                text = stringResource(R.string.compMDtext),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = { onDialogConfirm() }) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDialogDismiss() }) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        onDialogDismiss = { onDialogDismiss() })
}