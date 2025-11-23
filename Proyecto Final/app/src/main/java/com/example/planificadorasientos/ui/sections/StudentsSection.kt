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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.planificadorasientos.domain.model.Student
import com.example.planificadorasientos.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsSection(navController: NavController) {
    val context = LocalContext.current
    val studentViewModel: StudentViewModel = viewModel()
    val students by studentViewModel.students.collectAsState()

    LaunchedEffect(Unit) {
        studentViewModel.loadStudents()
    }

    var showDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    var showNoAssignedDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showNoCapacityDialog by remember { mutableStateOf(false) }

    val allFaculties = students.map { it.faculty }.distinct()
    val allCareers = students.map { it.career }.distinct()

    var selectedFaculty by remember { mutableStateOf<String?>(null) }
    var selectedCareer by remember { mutableStateOf<String?>(null) }
    var showOnlyUnassigned by remember { mutableStateOf(false) }
    val selectedIds = remember { mutableStateListOf<String>() }

    val filteredStudents = students.filter {
        (selectedFaculty == null || it.faculty == selectedFaculty) &&
                (selectedCareer == null || it.career == selectedCareer) &&
                (!showOnlyUnassigned || !it.assigned)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        // ===== HEADER =====
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
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd).zIndex(1f)
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
                            val studentsToExport = students.filter { it.assigned }
                            if (studentsToExport.isEmpty()) showNoAssignedDialog = true
                            else exportStudentsAsPlainText(context, studentsToExport)
                        }
                    )
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

        // ===== BOTONES SUPERIORES =====
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { editingStudent = null; showDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo Estudiante")
            }

            Button(
                onClick = {
                    selectedIds.forEach { id -> studentViewModel.assignSeat(id) }
                    selectedIds.clear()
                },
                enabled = selectedIds.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Asignar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Asignar Seleccionados")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== LISTA =====
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(filteredStudents) { student ->
                val isSelected = selectedIds.contains(student.id)
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                if (checked) selectedIds.add(student.id)
                                else selectedIds.remove(student.id)
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ID: ${student.id}")
                            Text("Nombre: ${student.name}")
                            Text("Facultad: ${student.faculty}")
                            Text("Carrera: ${student.career}")
                            Text("Asignado: ${if (student.assigned) "Sí" else "No"}")
                            if (student.assigned && student.place != null) Text("Lugar: ${student.place}")

                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                IconButton(onClick = { editingStudent = student; showDialog = true }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = {
                                    studentViewModel.deleteStudent(student.id)
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

        // ===== DIALOG NUEVO/EDITAR =====
        if (showDialog) {
            DialogNuevoEstudiante(
                initialStudent = editingStudent,
                onSave = { nuevo ->
                    if (editingStudent == null) studentViewModel.addStudent(nuevo)
                    else studentViewModel.updateStudent(nuevo)
                    showDialog = false
                },
                onCancel = { showDialog = false }
            )
        }

        if (showNoAssignedDialog) {
            AlertDialog(
                onDismissRequest = { showNoAssignedDialog = false },
                confirmButton = { TextButton(onClick = { showNoAssignedDialog = false }) { Text("Aceptar") } },
                title = { Text("Exportar Estudiantes") },
                text = { Text("No hay estudiantes asignados para exportar.") }
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        navController.navigate("login") { popUpTo(0) }
                    }) { Text("Sí, salir") }
                },
                dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") } },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Está seguro de que desea cerrar sesión?") }
            )
        }
    }
}

fun exportStudentsAsPlainText(context: Context, students: List<Student>) {
    val content = buildString {
        students.forEachIndexed { index, it ->
            append("ID: ${it.id}\n")
            append("Nombre: ${it.name}\n")
            append("Facultad: ${it.faculty}\n")
            append("Carrera: ${it.career}\n")
            append("Asignado: ${if (it.assigned) "Sí" else "No"}\n")
            append("Lugar: ${it.place ?: "-"}\n")
            if (index != students.lastIndex) append("------------------------------\n")
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
fun DropdownSelector(label: String, options: List<String>, selectedOption: String?, onOptionSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Filtrar por $label") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Todas") }, onClick = {
                onOptionSelected(null); expanded = false
            })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option); expanded = false
                })
            }
        }
    }
}

@Composable
fun DialogNuevoEstudiante(initialStudent: Student? = null, onSave: (Student) -> Unit, onCancel: () -> Unit) {
    var id by remember { mutableStateOf(initialStudent?.id ?: "") }
    var name by remember { mutableStateOf(initialStudent?.name ?: "") }
    var faculty by remember { mutableStateOf(initialStudent?.faculty ?: "") }
    var career by remember { mutableStateOf(initialStudent?.career ?: "") }

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = { TextButton(onClick = { onSave(Student(id, name, faculty, career)) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onCancel) { Text("Cancelar") } },
        title = { Text(if (initialStudent == null) "Nuevo Estudiante" else "Editar Estudiante") },
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
