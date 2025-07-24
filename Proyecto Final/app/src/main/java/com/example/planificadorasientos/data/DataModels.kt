package com.example.planificadorasientos.data

import androidx.compose.runtime.mutableStateListOf

data class Admin(
    val username: String,
    val password: String,
    val name: String
)

data class Student(
    val id: String,
    val name: String,
    val faculty: String,
    val career: String,
    val assigned: Boolean = false
)

data class Ceremony(
    val id: String,
    val faculty: String,
    val date: String,
    val time: String
)

object DataRepository {
    val students = mutableStateListOf(
        Student("2021001", "Luis Gómez", "Ingeniería", "Sistemas"),
        Student("2021002", "Ana Pérez", "Administración", "Contabilidad"),
        Student("2021003", "Juan Torres", "Ingeniería", "Industrial"),
        Student("2021004", "María García", "Medicina", "Medicina General"),
        Student("2021005", "Carlos Díaz", "Administración", "Mercadeo"),
        Student("2021006", "Pedro Peña", "Ingeniería", "Electrónica"),
        Student("2021007", "Lucía Reyes", "Medicina", "Odontología")
    )

    val ceremonies = mutableStateListOf(
        Ceremony("1", "Ingeniería", "2025-07-20", "10:00 AM"),
        Ceremony("2", "Derecho", "2025-07-20", "02:00 PM")
    )

    val uniqueFaculties: List<String>
        get() = students.map { it.faculty }.distinct()

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun removeStudentById(id: String) {
        students.removeIf { it.id == id }
    }

    fun updateStudent(updated: Student) {
        students.replaceAll { if (it.id == updated.id) updated else it }
    }

    fun addCeremony(ceremony: Ceremony) {
        ceremonies.add(ceremony)
    }

    fun updateCeremony(updated: Ceremony) {
        ceremonies.replaceAll { if (it.id == updated.id) updated else it }
    }

    fun removeCeremonyById(id: String) {
        ceremonies.removeIf { it.id == id }
    }
}
