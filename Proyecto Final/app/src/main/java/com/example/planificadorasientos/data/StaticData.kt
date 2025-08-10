package com.example.planificadorasientos.data
object StaticData {
    val ADMINS = listOf(
        Admin("admin", "123", "Director Académico"),
        Admin("coord", "456", "Coordinador")
    )

    val STUDENTS = listOf(
        Student("2021-0001", "Juan Pérez", "Ingeniería", "Sistemas"),
        Student("2021-0002", "María García", "Derecho", "Abogado"),
        Student("2021-0003", "Carlos López", "Derecho", "Abogado Litigante")
    )
}
