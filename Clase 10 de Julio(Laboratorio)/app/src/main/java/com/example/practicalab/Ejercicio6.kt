package com.example.practicalab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun CuadriculaSimple(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Cuadro(color = Color(0xFFEF5350), modifier = Modifier.weight(1f))
            Cuadro(color = Color(0xFF66BB6A), modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.weight(1f)) {
            Cuadro(color = Color(0xFF42A5F5), modifier = Modifier.weight(1f))
            Cuadro(color = Color(0xFFFFA726), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Cuadro(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        // Puedes poner un ícono, texto o dejarlo vacío
    }
}

@Preview(showBackground = true)
@Composable
fun CuadriculaSimplePreview() {
    PracticaLabTheme {
        CuadriculaSimple()
    }
}
