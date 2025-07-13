package com.example.practicalab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun TarjetaDeNoticia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .border(1.dp, Color.Gray)
            .padding(16.dp)
    ) {
        // Imagen simulada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Titular
        Text(
            text = "Jetpack Compose revoluciona el desarrollo Android",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Metadatos: autor y fecha
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(text = "Por Benjamin Bencosme", fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "13 de julio 2024", fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaDeNoticiaPreview() {
    PracticaLabTheme {
        TarjetaDeNoticia()
    }
}
