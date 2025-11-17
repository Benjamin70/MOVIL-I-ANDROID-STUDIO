package com.example.planificadorasientos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planificadorasientos.data.model.Student
import com.example.planificadorasientos.data.model.DataRepository
import com.example.planificadorasientos.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    // =======================
    // 🔹 CARGAR DATOS
    // =======================
    fun loadStudents() {
        viewModelScope.launch {
            try {
                val firebaseStudents = repository.getStudents()

                // 🔄 Sincronizar con DataRepository local
                DataRepository.students.clear()
                DataRepository.students.addAll(firebaseStudents)

                // Actualizar flujo observado por la UI
                _students.value = firebaseStudents
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 AGREGAR ESTUDIANTE
    // =======================
    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                repository.addStudent(student)
                DataRepository.addStudent(student)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 ACTUALIZAR ESTUDIANTE
    // =======================
    fun updateStudent(student: Student) {
        viewModelScope.launch {
            try {
                repository.updateStudent(student)
                DataRepository.updateStudent(student)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 ELIMINAR ESTUDIANTE
    // =======================
    fun deleteStudent(studentId: String) {
        viewModelScope.launch {
            try {
                repository.deleteStudent(studentId)
                DataRepository.removeStudentById(studentId)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 ASIGNAR ASIENTOS
    // =======================
    fun assignRandomSeat(studentId: String) {
        viewModelScope.launch {
            val place = DataRepository.assignRandomPlace(studentId)
            if (place.isNotEmpty()) {
                val student = DataRepository.students.firstOrNull { it.id == studentId }
                if (student != null) {
                    repository.updateStudent(student) // guardar en Firebase
                    loadStudents()
                }
            }
        }
    }

    // =======================
    // 🔹 DESASIGNAR ASIENTOS
    // =======================
    fun unassignSeat(studentId: String) {
        viewModelScope.launch {
            DataRepository.unassignPlace(studentId)
            val student = DataRepository.students.firstOrNull { it.id == studentId }
            if (student != null) {
                repository.updateStudent(student)
                loadStudents()
            }
        }
    }

    // =======================
    // 🔹 RESETEAR TODO
    // =======================
    fun resetAllSeats() {
        viewModelScope.launch {
            DataRepository.resetSeating()
            DataRepository.students.forEach {
                repository.updateStudent(it)
            }
            loadStudents()
        }
    }
}
