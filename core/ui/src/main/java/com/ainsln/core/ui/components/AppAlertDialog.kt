package com.ainsln.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ainsln.core.ui.R

@Composable
fun AppAlertDialog(
    title: String,
    text: String,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasCancelButton: Boolean = true
) {
    AlertDialog(
        title = { Text(title, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text) },
        onDismissRequest = { onDismissClick() },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(stringResource(R.string.ok_label))
            }
        },
        dismissButton = {
            if (hasCancelButton)
                TextButton(onClick = onDismissClick) {
                    Text(stringResource(R.string.cancel_label))
                }
        },
        modifier = modifier
    )
}
