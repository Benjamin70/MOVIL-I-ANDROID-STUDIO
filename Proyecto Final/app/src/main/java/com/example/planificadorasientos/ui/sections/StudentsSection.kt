package com.example.planificadorasientos.ui.sections

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.planificadorasientos.data.DataRepository
import com.example.planificadorasientos.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsSection(navController: NavController) { // <- ahora recibe navController
    var showDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }

    var showNoAssignedDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) } // <- diálogo de cerrar sesión
    var showNoCapacityDialog by remember { mutableStateOf(false) } // <- sin cupo disponible

    val context = LocalContext.current

    val allFaculties = DataRepository.uniqueFaculties
    val allCareers = DataRepository.students.map { it.career }.distinct()

    var selectedFaculty by remember { mutableStateOf<String?>(null) }
    var selectedCareer by remember { mutableStateOf<String?>(null) }
    var showOnlyUnassigned by remember { mutableStateOf(false) }

    val selectedIds = remember { mutableStateListOf<String>() }

    val filteredStudents = DataRepository.students.filter {
        (selectedFaculty == null || it.faculty == selectedFaculty) &&
                (selectedCareer == null || it.career == selectedCareer) &&
                (!showOnlyUnassigned || !it.assigned)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header con menú ANCLADO
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gestión de Estudiantes", style = MaterialTheme.typography.headlineSmall)
            }

            var menuExpanded by remember { mutableStateOf(false) }

            // 🔑 Anclamos el Dropdown al IconButton y lo elevamos sobre los filtros
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
                    .zIndex(1f)
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(0.dp, 4.dp),
                    properties = PopupProperties(focusable = true)
                ) {
                    DropdownMenuItem(
                        text = { Text("Exportar estudiantes asignados") },
                        onClick = {
                            menuExpanded = false
                            val studentsToExport = DataRepository.students.filter { it.assigned }
                            if (studentsToExport.isEmpty()) {
                                showNoAssignedDialog = true
                            } else {
                                exportStudentsAsPlainText(context, studentsToExport)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            menuExpanded = false
                            showLogoutDialog = true // <- pedir confirmación
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector("Facultad", allFaculties, selectedFaculty) { selectedFaculty = it }
        Spacer(modifier = Modifier.height(8.dp))
        DropdownSelector("Carrera", allCareers, selectedCareer) { selectedCareer = it }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showOnlyUnassigned, onCheckedChange = { showOnlyUnassigned = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mostrar solo pendientes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = {
                        editingStudent = null
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo Estudiante")
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = {
                        var anyFailed = false
                        selectedIds.forEach { id ->
                            val place = DataRepository.assignRandomPlace(id)
                            if (place.isEmpty()) anyFailed = true
                        }
                        selectedIds.clear()
                        if (anyFailed) showNoCapacityDialog = true
                    },
                    enabled = selectedIds.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Asignar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Asignar Seleccionados")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStudents) { student ->
                    val isSelected = selectedIds.contains(student.id)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        if (checked) selectedIds.add(student.id)
                                        else selectedIds.remove(student.id)
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ID: ${student.id}")
                                Text("Nombre: ${student.name}")
                                Text("Facultad: ${student.faculty}")
                                Text("Carrera: ${student.career}")
                                Text("Asignado: ${if (student.assigned) "Sí" else "No"}")
                                if (student.assigned && student.place != null) {
                                    Text("Lugar: ${student.place}") // 👈 Mostrar "Lugar"
                                }

                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    IconButton(onClick = {
                                        editingStudent = student
                                        showDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = {
                                        DataRepository.removeStudentById(student.id)
                                        selectedIds.remove(student.id)
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            DialogNuevoEstudiante(
                initialStudent = editingStudent,
                onSave = { nuevo ->
                    if (editingStudent == null) {
                        DataRepository.addStudent(nuevo)
                    } else {
                        DataRepository.removeStudentById(editingStudent!!.id)
                        DataRepository.addStudent(nuevo)
                    }
                    showDialog = false
                },
                onCancel = { showDialog = false }
            )
        }

        if (showNoAssignedDialog) {
            AlertDialog(
                onDismissRequest = { showNoAssignedDialog = false },
                confirmButton = {
                    TextButton(onClick = { showNoAssignedDialog = false }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Exportar Estudiantes") },
                text = { Text("No hay estudiantes asignados para exportar.") }
            )
        }

        // Aviso de capacidad llena
        if (showNoCapacityDialog) {
            AlertDialog(
                onDismissRequest = { showNoCapacityDialog = false },
                confirmButton = {
                    TextButton(onClick = { showNoCapacityDialog = false }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Sin cupos disponibles") },
                text = { Text("No fue posible asignar a todos. Verifica la disponibilidad por sección.") }
            )
        }

        // ===== Diálogo de confirmación de Cerrar sesión =====
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        // Navegar al login y limpiar el back stack
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }) {
                        Text("Sí, salir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Está seguro de que desea cerrar sesión?") }
            )
        }
    }
}

// ===== Exportación como TEXTO PLANO (sin archivos, sin FileProvider) =====
fun exportStudentsAsPlainText(context: Context, students: List<Student>) {
    val content = buildString {
        students.forEachIndexed { index, it ->
            append("ID: ${it.id}\n")
            append("Nombre: ${it.name}\n")
            append("Facultad: ${it.faculty}\n")
            append("Carrera: ${it.career}\n")
            append("Asignado: ${if (it.assigned) "Sí" else "No"}\n")
            append("Lugar: ${it.place ?: "-"}\n")
            if (index != students.lastIndex) {
                append("------------------------------\n")
            }
        }
    }

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Lista de estudiantes asignados")
        putExtra(Intent.EXTRA_TEXT, content)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Compartir estudiantes"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Filtrar por $label") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todas") },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DialogNuevoEstudiante(
    initialStudent: Student? = null,
    onSave: (Student) -> Unit,
    onCancel: () -> Unit
) {
    var id by remember { mutableStateOf(initialStudent?.id ?: "") }
    var name by remember { mutableStateOf(initialStudent?.name ?: "") }
    var faculty by remember { mutableStateOf(initialStudent?.faculty ?: "") }
    var career by remember { mutableStateOf(initialStudent?.career ?: "") }

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = {
                onSave(Student(id, name, faculty, career))
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        },
        title = {
            Text(if (initialStudent == null) "Nuevo Estudiante" else "Editar Estudiante")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID") })
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = faculty, onValueChange = { faculty = it }, label = { Text("Facultad") })
                OutlinedTextField(value = career, onValueChange = { career = it }, label = { Text("Carrera") })
            }
        }
    )
}
