package com.example.planificadorasientos.data.repository

import com.example.planificadorasientos.data.model.Student
import com.example.planificadorasientos.data.model.Ceremony
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

/**
 * FirebaseRepository
 * ------------------
 * Clase encargada de manejar la comunicación con Firebase Realtime Database.
 * Gestiona las operaciones CRUD de estudiantes y ceremonias.
 */
class FirebaseRepository {

    // Referencia a la base de datos de Firebase
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    // ------------------- STUDENTS -------------------

    suspend fun addStudent(student: Student) {
        try {
            db.child("students").child(student.id).setValue(student).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateStudent(student: Student) {
        try {
            db.child("students").child(student.id).setValue(student).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteStudent(studentId: String) {
        try {
            db.child("students").child(studentId).removeValue().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getStudents(): List<Student> {
        return try {
            val snapshot = db.child("students").get().await()
            snapshot.children.mapNotNull { it.getValue(Student::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // ------------------- CEREMONIES -------------------

    suspend fun addCeremony(ceremony: Ceremony) {
        try {
            db.child("ceremonies").child(ceremony.id).setValue(ceremony).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateCeremony(ceremony: Ceremony) {
        try {
            db.child("ceremonies").child(ceremony.id).setValue(ceremony).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteCeremony(ceremonyId: String) {
        try {
            db.child("ceremonies").child(ceremonyId).removeValue().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCeremonies(): List<Ceremony> {
        return try {
            val snapshot = db.child("ceremonies").get().await()
            snapshot.children.mapNotNull { it.getValue(Ceremony::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
