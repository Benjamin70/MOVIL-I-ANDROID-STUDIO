package com.example.planificadorasientos.data.local

import androidx.compose.runtime.mutableStateListOf
import com.example.planificadorasientos.domain.model.Student
import com.example.planificadorasientos.domain.model.Ceremony

/**
 * LocalDataSource
 * ------------------
 * Administra los datos en memoria de la app.
 * Se encarga del CRUD local de estudiantes y ceremonias.
 */
class LocalDataSource {

    // 🔹 Listas locales (en memoria)
    val students = mutableStateListOf<Student>()
    val ceremonies = mutableStateListOf<Ceremony>()

    val uniqueFaculties: List<String>
        get() = students.map { it.faculty }.distinct()

    // -------- CRUD LOCAL: ESTUDIANTES --------
    fun addStudent(student: Student) { students.add(student) }

    fun removeStudentById(id: String) {
        students.removeIf { it.id == id }
    }

    fun updateStudent(updated: Student) {
        val index = students.indexOfFirst { it.id == updated.id }
        if (index >= 0) students[index] = updated
    }

    // -------- CRUD LOCAL: CEREMONIAS --------
    fun addCeremony(ceremony: Ceremony) { ceremonies.add(ceremony) }

    fun updateCeremony(updated: Ceremony) {
        val index = ceremonies.indexOfFirst { it.id == updated.id }
        if (index >= 0) ceremonies[index] = updated
    }

    fun removeCeremonyById(id: String) {
        ceremonies.removeIf { it.id == id }
    }
}
