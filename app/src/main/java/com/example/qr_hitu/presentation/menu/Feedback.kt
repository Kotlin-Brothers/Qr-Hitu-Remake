package com.example.qr_hitu.presentation.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.qr_hitu.functions.saveFeedback
import com.example.qr_hitu.presentation.ui.MainDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Feedback(navController: NavController){

    var enabled by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf("") }
    val infoDialog by remember { mutableStateOf(false) }
    var thxDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold {
        Box (modifier = Modifier.padding(horizontal = 16.dp)) {
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
                    text = stringResource(R.string.feedbackMessage),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    value = feedback,
                    onValueChange = { feedback = it; if(feedback!="") enabled = true else enabled = false},
                    label = { Text("Feedback") },
                    placeholder = { Text("Feedback") },
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

                Spacer(modifier = Modifier.padding(5.dp))

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        //TODO - open dialog
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.feedbackAlt),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Button(
                    onClick = {
                        saveFeedback(feedback)
                        feedback = ""
                        thxDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    enabled = enabled
                ) {
                    Text(
                        stringResource(R.string.problemSend),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (infoDialog) {
                    //TODO info Dialog
                }
                if (thxDialog) {
                    MainDialog(
                        title = { stringResource(R.string.feedbackTitle) },
                        bodyText = { Text(stringResource(R.string.feedbackText), style = MaterialTheme.typography.bodyMedium) },
                        confirmButton = { TextButton(onClick = { thxDialog = false }) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        } },
                        dismissButton = { }
                    ) {}
                }
            }
        }
    }
}