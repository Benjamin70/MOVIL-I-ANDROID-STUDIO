package com.example.planificadorasientos.ui.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.planificadorasientos.domain.model.Ceremony
import com.example.planificadorasientos.domain.model.Student
import com.example.planificadorasientos.ui.viewmodel.CeremonyViewModel
import com.example.planificadorasientos.ui.viewmodel.StudentViewModel

@Composable
fun DashboardSection(navController: NavController) {
    val studentViewModel: StudentViewModel = viewModel()
    val ceremonyViewModel: CeremonyViewModel = viewModel()

    val students by studentViewModel.students.collectAsState(initial = emptyList())
    val ceremonies by ceremonyViewModel.ceremonies.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        studentViewModel.loadStudents()
        ceremonyViewModel.loadCeremonies()
    }

    val ceremonyCount = ceremonies.size
    val studentCount = students.size
    val assignedCount = students.count { it.assigned }
    val pendingCount = studentCount - assignedCount

    var showCeremoniesDialog by remember { mutableStateOf(false) }
    var showStudentsDialog by remember { mutableStateOf(false) }
    var showPendingDialog by remember { mutableStateOf(false) }
    var showAssignedDialog by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Assessment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Resumen General", style = MaterialTheme.typography.headlineSmall)
            }

            Box {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                }
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            expandedMenu = false
                            showLogoutConfirm = true
                        }
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            MetricCard("Ceremonias", ceremonyCount.toString(), Icons.Default.Event) { showCeremoniesDialog = true }
            MetricCard("Estudiantes", studentCount.toString(), Icons.Default.Group) { showStudentsDialog = true }
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            MetricCard("Pendientes", pendingCount.toString(), Icons.Default.HourglassBottom) { showPendingDialog = true }
            MetricCard("Asignados", assignedCount.toString(), Icons.Default.CheckCircle) { showAssignedDialog = true }
        }
    }

    if (showCeremoniesDialog) {
        CeremoniesDialog(ceremonies) { showCeremoniesDialog = false }
    }

    if (showStudentsDialog) {
        StudentsDialog("Listado de Estudiantes", students, showPlace = true) { showStudentsDialog = false }
    }

    if (showPendingDialog) {
        StudentsDialog(
            "Listado de Estudiantes Pendientes",
            students.filter { !it.assigned },
            showPlace = false
        ) { showPendingDialog = false }
    }

    if (showAssignedDialog) {
        AssignedSeatsDialog(students, onClose = { showAssignedDialog = false })
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Está seguro de que desea cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("No") }
            }
        )
    }
}

@Composable
fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(120.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 14.sp)
        }
    }
}

@Composable
private fun CeremoniesDialog(ceremonies: List<Ceremony>, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { TextButton(onClick = onClose) { Text("Cerrar") } },
        title = { Text("Listado de Ceremonias") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                items(ceremonies) { c -> CeremonyRow(c) }
            }
        }
    )
}

@Composable
private fun CeremonyRow(c: Ceremony) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(c.faculty, fontWeight = FontWeight.SemiBold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(c.date)
            Text(c.time, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun StudentsDialog(title: String, students: List<Student>, showPlace: Boolean, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { TextButton(onClick = onClose) { Text("Cerrar") } },
        title = { Text(title) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                items(students) { s -> StudentRow(s, showPlace) }
            }
        }
    )
}

@Composable
private fun StudentRow(s: Student, showPlace: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(s.name, fontWeight = FontWeight.SemiBold)
                if (showPlace) Text(s.career, style = MaterialTheme.typography.bodySmall)
            }
            Text(s.faculty, style = MaterialTheme.typography.bodySmall)
        }
        if (!showPlace) {
            Spacer(Modifier.width(8.dp))
            Text("Pendiente")
        }
    }
}

@Composable
private fun AssignedSeatsDialog(students: List<Student>, onClose: () -> Unit) {
    val assigned = students.filter { it.assigned && it.place != null }
    var selected by remember { mutableStateOf<Student?>(null) }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { TextButton(onClick = onClose) { Text("Cerrar") } },
        title = { Text("Estudiantes Asignados") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                items(assigned) { student ->
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable { selected = student },
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("🧑‍🎓", fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    )

    selected?.let { st ->
        AssignedStudentDetailDialog(student = st, onClose = { selected = null })
    }
}

@Composable
private fun AssignedStudentDetailDialog(student: Student, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = { TextButton(onClick = onClose) { Text("Cerrar") } },
        title = { Text("Detalles del Estudiante") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧑‍🎓", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(student.name, fontWeight = FontWeight.SemiBold)
                        Text(student.faculty, style = MaterialTheme.typography.bodySmall)
                    }
                    AssistChip(
                        onClick = {},
                        label = { Text("Asignado") },
                        leadingIcon = {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFDFF3E0),
                            labelColor = Color(0xFF2E7D32)
                        )
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Asiento:")
                    Text(student.place ?: "-", fontWeight = FontWeight.Medium)
                }
            }
        }
    )
}
