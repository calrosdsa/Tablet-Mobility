package com.coppernic.mobility.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DialogConfirmation(
    onDismiss:()->Unit,
    onAction:()->Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onAction()
            })
            { Text(text = "OK") }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            })
            { Text(text = "Cancel") }
        },
        title = { Text(text = "¿Continuar con la acción solicitada?") },
        text = { Text(text ="Porfavor confirme si desea continuar"  ) }
    )
}