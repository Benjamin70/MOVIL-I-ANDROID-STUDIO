package com.example.planificadorasientos.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.planificadorasientos.ui.viewmodel.StudentViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val studentViewModel: StudentViewModel = viewModel()
    val students by studentViewModel.students.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(true) }
    var isRegisterMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    // Cargar estudiantes al abrir la pantalla
    LaunchedEffect(Unit) {
        studentViewModel.loadStudents()
    }

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
                        username = ""
                        password = ""
                        showError = false
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
                        password = ""
                        showError = false
                    },
                    label = { Text("Estudiante") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; showError = false },
            label = { Text(if (isAdmin) "Correo electrónico" else "ID Estudiante") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError
        )

        Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                    Text(
                        if (isRegisterMode) "Ya tengo cuenta (Iniciar sesión)"
                        else "¿No tienes cuenta? Regístrate",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                isLoading = true
                if (isAdmin) {
                    val email = username.trim()
                    val pass = password.trim()
                    if (isRegisterMode) {
                        // 🔹 Registrar nuevo administrador
                        auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    showSuccessDialog = true
                                    isRegisterMode = false
                                    username = ""
                                    password = ""
                                } else {
                                    errorMessage =
                                        "Error al registrarse: ${task.exception?.localizedMessage}"
                                    showError = true
                                }
                            }
                    } else {
                        // 🔹 Iniciar sesión admin
                        auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    navController.navigate("admin_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = "Credenciales inválidas o usuario no registrado"
                                    showError = true
                                }
                            }
                    }
                } else {
                    // 🔸 Estudiante
                    val inputId = username.trim().replace("-", "")
                    val student = students.find {
                        it.id.replace("-", "").equals(inputId, ignoreCase = true)
                    }
                    isLoading = false
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
            enabled = if (isAdmin)
                username.isNotBlank() && password.isNotBlank()
            else username.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(
                    text = if (isAdmin && isRegisterMode) "Registrarse" else "Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
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
                    text = if (isAdmin)
                        "Crea tu cuenta con correo y contraseña."
                    else
                        "• ID: 2021-001, 2021-002, 2021-003",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Registro exitoso") },
            text = { Text("Usuario registrado exitosamente. Ahora puede iniciar sesión.") }
        )
    }
}
