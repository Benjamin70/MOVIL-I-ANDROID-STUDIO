package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListaDeTareas(modifier: Modifier = Modifier) {
    var nuevaTarea by remember { mutableStateOf("") }
    val tareas = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = nuevaTarea,
                onValueChange = { nuevaTarea = it },
                modifier = Modifier.weight(1f),
                label = { Text("Nueva tarea") }
            )
            Button(
                onClick = {
                    if (nuevaTarea.isNotBlank()) {
                        tareas.add(nuevaTarea)
                        nuevaTarea = ""
                    }
                }
            ) {
                Text("Añadir")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tareas) { tarea ->
                Text(text = "• $tarea", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaDeTareasPreview() {
    ListaDeTareas()
}
