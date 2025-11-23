package com.example.planificadorasientos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planificadorasientos.domain.model.Student
import com.example.planificadorasientos.data.repository.StudentRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona los estudiantes usando el repositorio.
 * Actualiza automáticamente la UI mediante StateFlow.
 */
class StudentViewModel : ViewModel() {

    private val repository = StudentRepositoryImpl()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    // =========================
    // 🔹 CARGAR ESTUDIANTES
    // =========================
    fun loadStudents() {
        viewModelScope.launch {
            try {
                val list = repository.getStudents()
                _students.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =========================
    // 🔹 AGREGAR ESTUDIANTE
    // =========================
    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                repository.addStudent(student)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =========================
    // 🔹 ACTUALIZAR ESTUDIANTE
    // =========================
    fun updateStudent(student: Student) {
        viewModelScope.launch {
            try {
                repository.updateStudent(student)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =========================
    // 🔹 ELIMINAR ESTUDIANTE
    // =========================
    fun deleteStudent(studentId: String) {
        viewModelScope.launch {
            try {
                repository.deleteStudent(studentId)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =========================
    // 🔹 ASIGNAR / DESASIGNAR ASIENTOS
    // =========================
    fun assignSeat(studentId: String) {
        viewModelScope.launch {
            try {
                repository.assignSeat(studentId)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unassignSeat(studentId: String) {
        viewModelScope.launch {
            try {
                repository.unassignSeat(studentId)
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetAllSeats() {
        viewModelScope.launch {
            try {
                repository.resetAllSeats()
                loadStudents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
