package com.example.practicalab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalab.ui.theme.PracticaLabTheme

@Composable
fun IconosSociales(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🐦", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "💼", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "📸", fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun IconosSocialesPreview() {
    PracticaLabTheme {
        IconosSociales()
    }
}
