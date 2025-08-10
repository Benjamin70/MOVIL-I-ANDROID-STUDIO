package com.example.planificadorasientos.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChairAlt
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.planificadorasientos.data.DataRepository
import com.example.planificadorasientos.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(
    navController: NavController,
    studentId: String
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // ===== Estado de búsqueda / alumno seleccionado =====
    var queryId by remember { mutableStateOf(studentId) }
    var lookedUp by remember { mutableStateOf(false) }
    var student by remember {
        mutableStateOf(
            DataRepository.students.find { normalizeId(it.id) == normalizeId(studentId) }
        )
    }

    // si cambia el parámetro de la ruta, actualizamos todo
    LaunchedEffect(studentId) {
        queryId = studentId
        student = DataRepository.students.find { normalizeId(it.id) == normalizeId(studentId) }
        lookedUp = false
    }

    BackHandler { showExitDialog = true }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Deseas cerrar sesión y volver a la pantalla de inicio?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }) { Text("Sí") }
            },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text("No") } }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ===== Header =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Portal Estudiante",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        val nombre = student?.name?.split(" ")?.firstOrNull() ?: "Estudiante"
                        Text(
                            text = "Hola $nombre, bienvenido",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }
                IconButton(onClick = { showExitDialog = true }) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ===== Buscador por ID (filtro) =====
        OutlinedTextField(
            value = queryId,
            onValueChange = { queryId = it },
            label = { Text("Buscar por ID") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                student = DataRepository.students.find { normalizeId(it.id) == normalizeId(queryId) }
                lookedUp = true
            },
            enabled = queryId.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) { Text("Consultar") }

        if (lookedUp && student == null) {
            Spacer(Modifier.height(8.dp))
            Text(
                "ID no encontrado",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(16.dp))

        // ===== Tarjeta de "Evento" -> Facultad y Carrera =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Mi Información Académica",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    if (student != null) {
                        Text(
                            text = "Facultad: ${student!!.faculty}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "Carrera: ${student!!.career}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                        )
                    } else {
                        Text(
                            text = "No se encontró el estudiante",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ===== Tarjeta de Asiento =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ChairAlt,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Mi Asiento Asignado",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (student != null) {
                        Text("Nombre: ${student!!.name}")
                        Text(
                            text = "Asiento: ${student!!.place ?: "Sin asiento asignado"}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                        )
                    } else {
                        Text(
                            text = "—",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}


private fun normalizeId(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    if (digits.length < 5) return digits
    val year = digits.take(4)
    val serial = digits.drop(4).padStart(4, '0')
    return year + serial
}
