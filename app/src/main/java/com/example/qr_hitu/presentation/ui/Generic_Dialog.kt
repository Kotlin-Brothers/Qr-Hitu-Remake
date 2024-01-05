package com.example.qr_hitu.presentation.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign


@Composable
fun MainDialog(
    title: @Composable () -> String,
    bodyText: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    onDialogDismiss: () -> Unit
) {
    val dialogTitle = title()
    AlertDialog(
        title = {
            Text(
                text = dialogTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = { bodyText() },
        textContentColor = MaterialTheme.colorScheme.onSecondary,
        titleContentColor = MaterialTheme.colorScheme.onSecondary,
        confirmButton = { confirmButton() },
        onDismissRequest = { onDialogDismiss() },
        dismissButton = { dismissButton() }
    )
}