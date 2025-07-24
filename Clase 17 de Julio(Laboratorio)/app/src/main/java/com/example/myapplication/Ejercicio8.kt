package com.example.myapplication

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TarjetaExpandible(modifier: Modifier = Modifier) {
    var expandido by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray)
            .clickable { expandido = !expandido }
            .padding(16.dp)
    ) {
        Text(text = "Título de la tarjeta", fontSize = 20.sp)

        if (expandido) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Este es el contenido detallado que aparece cuando la tarjeta está expandida.",
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaExpandiblePreview() {
    TarjetaExpandible()
}
