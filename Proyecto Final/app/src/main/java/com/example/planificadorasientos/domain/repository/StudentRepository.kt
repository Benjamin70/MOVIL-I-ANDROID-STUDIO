package com.example.planificadorasientos.domain.repository

import com.example.planificadorasientos.domain.model.Student

interface StudentRepository {

    suspend fun getStudents(): List<Student>

    suspend fun addStudent(student: Student)

    suspend fun updateStudent(student: Student)

    suspend fun deleteStudent(studentId: String)

    suspend fun assignSeat(studentId: String): String

    suspend fun unassignSeat(studentId: String)

    suspend fun resetAllSeats()
}
