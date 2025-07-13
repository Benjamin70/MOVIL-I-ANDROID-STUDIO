package com.example.practicalab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun BarraDeProgreso(progreso: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progreso.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(Color(0xFF2196F3)) // azul
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BarraDeProgresoPreview() {
    PracticaLabTheme {
        Column(modifier = Modifier.padding(24.dp)) {
            BarraDeProgreso(progreso = 0.25f)
            Spacer(modifier = Modifier.height(12.dp))
            BarraDeProgreso(progreso = 0.5f)
            Spacer(modifier = Modifier.height(12.dp))
            BarraDeProgreso(progreso = 0.8f)
        }
    }
}
