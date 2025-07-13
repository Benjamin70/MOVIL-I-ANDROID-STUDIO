package com.example.practicalab

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun PerfilDeUsuario(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🧑‍💻",
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "fede_dev",
                    fontSize = 18.sp
                )
                Text(
                    text = "En línea",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilDeUsuarioPreview() {
    PracticaLabTheme {
        PerfilDeUsuario()
    }
}
