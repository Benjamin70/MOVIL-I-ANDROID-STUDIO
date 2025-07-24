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
fun EspejoDeTexto(modifier: Modifier = Modifier) {
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
            label = { Text("Escribe algo...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tú estás escribiendo: $texto",
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EspejoDeTextoPreview() {
    EspejoDeTexto()
}
