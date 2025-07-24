package com.example.planificadorasientos.ui.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorasientos.data.DataRepository
import com.example.planificadorasientos.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }

    val allFaculties = DataRepository.uniqueFaculties
    val allCareers = DataRepository.students.map { it.career }.distinct()

    var selectedFaculty by remember { mutableStateOf<String?>(null) }
    var selectedCareer by remember { mutableStateOf<String?>(null) }

    val filteredStudents = DataRepository.students.filter {
        (selectedFaculty == null || it.faculty == selectedFaculty) &&
                (selectedCareer == null || it.career == selectedCareer)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)) {

        // Encabezado
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Group, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Gestión de Estudiantes", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        DropdownSelector("Facultad", allFaculties, selectedFaculty) { selectedFaculty = it }
        Spacer(modifier = Modifier.height(8.dp))
        DropdownSelector("Carrera", allCareers, selectedCareer) { selectedCareer = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón agregar
        Button(
            onClick = {
                editingStudent = null
                showDialog = true
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo Estudiante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable lista
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStudents) { student ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${student.id}")
                            Text("Nombre: ${student.name}")
                            Text("Facultad: ${student.faculty}")
                            Text("Carrera: ${student.career}")
                            Text("Asignado: ${if (student.assigned) "Sí" else "No"}")

                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                IconButton(onClick = {
                                    editingStudent = student
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = {
                                    DataRepository.removeStudentById(student.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
    }
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
