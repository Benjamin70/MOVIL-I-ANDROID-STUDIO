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
fun AppDeSaludos(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var saludo by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingresa tu nombre") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            saludo = if (nombre.isNotBlank()) "¡Hola, $nombre!" else "Por favor, escribe tu nombre"
        }) {
            Text("Saludar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = saludo, fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun AppDeSaludosPreview() {
    AppDeSaludos()
}
