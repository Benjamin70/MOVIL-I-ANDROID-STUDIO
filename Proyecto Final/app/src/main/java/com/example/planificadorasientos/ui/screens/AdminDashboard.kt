package com.example.planificadorasientos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorasientos.ui.sections.CeremoniesSection
import com.example.planificadorasientos.ui.sections.StudentsSection
import com.example.planificadorasientos.ui.sections.DashboardSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Dashboard", "Ceremonias", "Estudiantes")
    val icons = listOf(Icons.Default.Dashboard, Icons.Default.Event, Icons.Default.Group)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Panel",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Panel de Administrador", fontSize = 20.sp)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = tab) },
                        label = { Text(tab) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> DashboardSection()
                1 -> CeremoniesSection()
                2 -> StudentsSection()
            }
        }
    }
}
