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
fun FormularioSimple(modifier: Modifier = Modifier) {
    var texto by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Escribe algo") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Acción del botón */ },
            enabled = texto.isNotEmpty()
        ) {
            Text("Enviar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (texto.isEmpty()) "El botón se habilita al escribir" else "¡Listo para enviar!",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioSimplePreview() {
    FormularioSimple()
}
