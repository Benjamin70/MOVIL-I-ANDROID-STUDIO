package com.example.planificadorasientos.data.repository

import com.example.planificadorasientos.data.local.LocalDataSource
import com.example.planificadorasientos.domain.model.Student
import com.example.planificadorasientos.domain.repository.StudentRepository
import com.example.planificadorasientos.domain.usecase.SeatAssignmentUseCase

/**
 * Implementación concreta del repositorio de estudiantes.
 * Combina la fuente local (LocalDataSource), Firebase y la lógica de asientos (SeatAssignmentUseCase).
 */
class StudentRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSource(),
    private val firebaseRepository: FirebaseRepository = FirebaseRepository(),
    private val seatAssignmentUseCase: SeatAssignmentUseCase = SeatAssignmentUseCase()
) : StudentRepository {

    override suspend fun getStudents(): List<Student> {
        val firebaseStudents = firebaseRepository.getStudents()
        localDataSource.students.clear()
        localDataSource.students.addAll(firebaseStudents)
        return localDataSource.students
    }

    override suspend fun addStudent(student: Student) {
        firebaseRepository.addStudent(student)
        localDataSource.students.add(student)
    }

    override suspend fun updateStudent(student: Student) {
        firebaseRepository.updateStudent(student)
        localDataSource.updateStudent(student)
    }

    override suspend fun deleteStudent(studentId: String) {
        firebaseRepository.deleteStudent(studentId)
        localDataSource.removeStudentById(studentId)
    }

    override suspend fun assignSeat(studentId: String): String {
        val seat = seatAssignmentUseCase.assignRandomPlace(localDataSource.students, studentId)
        val student = localDataSource.students.firstOrNull { it.id == studentId }
        if (student != null) firebaseRepository.updateStudent(student)
        return seat
    }

    override suspend fun unassignSeat(studentId: String) {
        seatAssignmentUseCase.unassignPlace(localDataSource.students, studentId)
        val student = localDataSource.students.firstOrNull { it.id == studentId }
        if (student != null) firebaseRepository.updateStudent(student)
    }

    override suspend fun resetAllSeats() {
        seatAssignmentUseCase.resetSeating(localDataSource.students)
        localDataSource.students.forEach {
            firebaseRepository.updateStudent(it)
        }
    }
}
