package com.example.planificadorasientos.data

import com.example.planificadorasientos.data.model.Admin

object StaticData {
    // 🔐 Solo dejamos admins para el login temporal
    val ADMINS = listOf(
        Admin(username = "admin", password = "123", name = "Director Académico"),
        Admin(username = "coord", password = "456", name = "Coordinador de Ceremonias")
    )
}
