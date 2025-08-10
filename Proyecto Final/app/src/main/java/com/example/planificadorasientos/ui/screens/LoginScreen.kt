package com.example.planificadorasientos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.planificadorasientos.data.StaticData
import com.example.planificadorasientos.data.DataRepository
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = "School Icon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Planificador de Asientos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Sistema de Graduaciones",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = isAdmin,
                    onClick = {
                        isAdmin = true
                        username = ""; password = ""; showError = false
                    },
                    label = { Text("Administrador") },
                    leadingIcon = { Icon(Icons.Default.AdminPanelSettings, null) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = !isAdmin,
                    onClick = {
                        isAdmin = false
                        password = ""; showError = false
                    },
                    label = { Text("Estudiante") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Usuario / ID
        OutlinedTextField(
            value = username,
            onValueChange = { username = it; showError = false },
            label = { Text(if (isAdmin) "Usuario" else "ID Estudiante") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError
        )

        Spacer(Modifier.height(16.dp))

        // Contraseña SOLO para Admin
        if (isAdmin) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; showError = false },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showError
            )
            Spacer(Modifier.height(8.dp))
        }

        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (isAdmin) {
                    val admin = StaticData.ADMINS.find { it.username == username && it.password == password }
                    if (admin != null) {
                        navController.navigate("admin_dashboard")
                    } else {
                        errorMessage = "Usuario o contraseña incorrectos"
                        showError = true
                    }
                } else {
                    // Normalizar para aceptar con o sin guion
                    val inputId = username.trim().replace("-", "")
                    val student = DataRepository.students.find {
                        it.id.replace("-", "").equals(inputId, ignoreCase = true)
                    }
                    if (student != null) {
                        navController.navigate("student_dashboard/${student.id}")
                    } else {
                        errorMessage = "ID de estudiante no válido"
                        showError = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = if (isAdmin) username.isNotBlank() && password.isNotBlank()
            else username.isNotBlank()
        ) {
            Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Credenciales de Prueba:",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (isAdmin) {
                        "• admin / 123\n• coord / 456"
                    } else {
                        "• ID: 2021-001, 2021-002, 2021-003"
                    },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
