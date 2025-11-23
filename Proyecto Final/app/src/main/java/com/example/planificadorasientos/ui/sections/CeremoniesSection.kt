package com.example.planificadorasientos.ui.sections

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.planificadorasientos.domain.model.Ceremony
import com.example.planificadorasientos.ui.viewmodel.CeremonyViewModel
import com.example.planificadorasientos.ui.viewmodel.StudentViewModel
import java.util.*

@Composable
fun CeremoniesSection(navController: NavController) {
    val context = LocalContext.current
    val ceremonyViewModel: CeremonyViewModel = viewModel()
    val ceremonies by ceremonyViewModel.ceremonies.collectAsState(initial = emptyList())

    val studentViewModel: StudentViewModel = viewModel()
    val students by studentViewModel.students.collectAsState(initial = emptyList())

    // Cargar ceremonias y estudiantes al abrir
    LaunchedEffect(Unit) {
        ceremonyViewModel.loadCeremonies()
        studentViewModel.loadStudents()
    }

    // Facultades únicas obtenidas desde Firebase
    val facultiesList = students.map { it.faculty.trim() }.distinct()

    var showDialog by remember { mutableStateOf(false) }
    var editingCeremony by remember { mutableStateOf<Ceremony?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // ===== HEADER =====
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Gestión", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gestión de Ceremonias", style = MaterialTheme.typography.headlineSmall)
            }

            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(0.dp, 4.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            menuExpanded = false
                            showLogoutDialog = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                editingCeremony = null
                showDialog = true
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nueva Ceremonia")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== LISTA DE CEREMONIAS =====
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ceremonies) { ceremony ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Facultad: ${ceremony.faculty}")
                        Text("Fecha: ${ceremony.date}")
                        Text("Hora: ${ceremony.time}")
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                editingCeremony = ceremony
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                ceremonyViewModel.deleteCeremony(ceremony.id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    // ===== DIALOG NUEVO/EDITAR CEREMONIA =====
    if (showDialog) {
        DialogNuevaCeremonia(
            initialCeremony = editingCeremony,
            existingFaculties = facultiesList,
            onSave = { nueva ->
                if (editingCeremony == null) {
                    ceremonyViewModel.addCeremony(nueva)
                } else {
                    ceremonyViewModel.updateCeremony(nueva)
                }
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }

    // ===== DIALOG LOGOUT =====
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    navController.navigate("login") { popUpTo(0) }
                }) { Text("Sí, salir") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Está seguro de que desea cerrar sesión?") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogNuevaCeremonia(
    initialCeremony: Ceremony? = null,
    existingFaculties: List<String>,
    onSave: (Ceremony) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var faculty by remember { mutableStateOf(initialCeremony?.faculty ?: "") }
    var date by remember { mutableStateOf(initialCeremony?.date ?: "") }
    var time by remember { mutableStateOf(initialCeremony?.time ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = {
                val id = initialCeremony?.id ?: System.currentTimeMillis().toString()
                onSave(Ceremony(id = id, faculty = faculty, date = date, time = time))
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancelar") }
        },
        title = { Text(if (initialCeremony == null) "Nueva Ceremonia" else "Editar Ceremonia") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dropdown Facultad (dinámico desde Firebase)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = faculty,
                        onValueChange = {},
                        label = { Text("Facultad") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clickable { expanded = true }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        existingFaculties.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    faculty = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker(context) { date = it } }
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    label = { Text("Hora") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker(context) { time = it } }
                )
            }
        }
    )
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, day ->
            onDateSelected("$year-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            val amPm = if (hour >= 12) "PM" else "AM"
            val displayHour = if (hour % 12 == 0) 12 else hour % 12
            onTimeSelected(String.format("%02d:%02d %s", displayHour, minute, amPm))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    ).show()
}
