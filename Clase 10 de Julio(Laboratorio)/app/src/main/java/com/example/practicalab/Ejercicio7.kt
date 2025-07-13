package com.example.practicalab

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun ItemDeRecibo(nombre: String, precio: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = nombre, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = precio, fontSize = 16.sp)
    }
}

@Composable
fun ReciboCompleto(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        ItemDeRecibo("Café Americano", "$2.50")
        ItemDeRecibo("Croissant", "$1.80")
        ItemDeRecibo("Jugo de Naranja", "$3.00")
        ItemDeRecibo("Té Verde", "$2.00")
    }
}

@Preview(showBackground = true)
@Composable
fun ReciboCompletoPreview() {
    PracticaLabTheme {
        ReciboCompleto()
    }
}
