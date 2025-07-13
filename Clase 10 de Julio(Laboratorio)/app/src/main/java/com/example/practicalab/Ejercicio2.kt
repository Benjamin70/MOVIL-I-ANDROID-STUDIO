package com.example.practicalab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun TarjetaDePresentacion(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Text(
            text = "Federico Bencosme",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Desarrollador Android Jr.",
            fontSize = 18.sp,
            color = Color.Gray
        )
        Text(
            text = "fede.bencosme@email.com",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaDePresentacionPreview() {
    PracticaLabTheme {
        TarjetaDePresentacion()
    }
}
