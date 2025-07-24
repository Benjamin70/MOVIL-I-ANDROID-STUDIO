package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MensajeSecreto(modifier: Modifier = Modifier) {
    var mostrarMensaje by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { mostrarMensaje = !mostrarMensaje }) {
            Text(text = if (mostrarMensaje) "Ocultar Secreto" else "Revelar Secreto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mostrarMensaje) {
            Text(text = "🤫 Este es el mensaje secreto.", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MensajeSecretoPreview() {
    MensajeSecreto()
}
