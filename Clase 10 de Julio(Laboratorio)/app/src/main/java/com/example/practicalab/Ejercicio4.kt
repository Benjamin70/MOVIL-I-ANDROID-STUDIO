package com.example.practicalab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun ArticuloSimple(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Text(
            text = "Aprendiendo Jetpack Compose",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Jetpack Compose es el nuevo toolkit de UI moderno de Android que simplifica y acelera el desarrollo de interfaces de usuario. Al estar basado en funciones @Composable, permite escribir código de manera más declarativa, haciendo que la interfaz reaccione automáticamente a los cambios de estado. Además, mejora la productividad gracias a herramientas como previews, navegación más sencilla y menos boilerplate en comparación con XML."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticuloSimplePreview() {
    PracticaLabTheme {
        ArticuloSimple()
    }
}
