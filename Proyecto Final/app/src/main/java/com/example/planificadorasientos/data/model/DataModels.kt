package com.example.planificadorasientos.data.model

import androidx.compose.runtime.mutableStateListOf

// ---------- MODELOS DE DATOS ----------

data class Admin(
    val username: String = "",
    val password: String = "",
    val name: String = ""
)

data class Student(
    val id: String = "",
    val name: String = "",
    val faculty: String = "",
    val career: String = "",
    val assigned: Boolean = false,
    val place: String? = null
)

data class Ceremony(
    val id: String = "",
    val faculty: String = "",
    val date: String = "",
    val time: String = ""
)

// ---------- REPOSITORIO LOCAL TEMPORAL ----------

object DataRepository {

    // 🔹 Estas listas ahora se llenan desde Firebase al iniciar la app (por los ViewModel)
    val students = mutableStateListOf<Student>()
    val ceremonies = mutableStateListOf<Ceremony>()

    val uniqueFaculties: List<String>
        get() = students.map { it.faculty }.distinct()

    // -------- CRUD LOCAL (usado por los ViewModel) --------

    fun addStudent(student: Student) { students.add(student) }

    fun removeStudentById(id: String) { students.removeIf { it.id == id } }

    fun updateStudent(updated: Student) {
        val idx = students.indexOfFirst { it.id == updated.id }
        if (idx >= 0) students[idx] = updated
    }

    fun addCeremony(ceremony: Ceremony) { ceremonies.add(ceremony) }

    fun updateCeremony(updated: Ceremony) {
        val idx = ceremonies.indexOfFirst { it.id == updated.id }
        if (idx >= 0) ceremonies[idx] = updated
    }

    fun removeCeremonyById(id: String) { ceremonies.removeIf { it.id == id } }

    // -------- LÓGICA DE ASIGNACIÓN --------

    private val sectionsLetters: List<Char> = ('A'..'Z').toList()
    private const val capacityPerSection: Int = 12
    private val seatingState: MutableMap<Char, MutableSet<Int>> = mutableMapOf()

    /** Asigna aleatoriamente una sección (A..Z) y fila (1..12) a un estudiante. */
    fun assignRandomPlace(studentId: String): String {
        val student = students.firstOrNull { it.id == studentId } ?: return ""
        if (student.assigned && student.place != null) return student.place

        val availableSections = sectionsLetters.filter { letter ->
            val occupied = seatingState[letter]?.size ?: 0
            occupied < capacityPerSection
        }
        if (availableSections.isEmpty()) return ""

        val section = availableSections.random()
        val occupiedRows = seatingState.getOrPut(section) { mutableSetOf() }
        val freeRows = (1..capacityPerSection).filter { it !in occupiedRows }
        if (freeRows.isEmpty()) return ""
        val row = freeRows.random()

        occupiedRows.add(row)
        val place = formatPlace(section, row)
        updateStudent(student.copy(assigned = true, place = place))
        return place
    }

    /** Libera el asiento de un estudiante. */
    fun unassignPlace(studentId: String) {
        val student = students.firstOrNull { it.id == studentId } ?: return
        val place = student.place ?: return
        val parsed = parsePlace(place) ?: return
        val (section, row) = parsed
        seatingState[section]?.remove(row)
        updateStudent(student.copy(assigned = false, place = null))
    }

    /** Reinicia todos los asientos asignados. */
    fun resetSeating() {
        seatingState.clear()
        val snapshot = students.toList()
        snapshot.forEach { st ->
            if (st.assigned || st.place != null) {
                updateStudent(st.copy(assigned = false, place = null))
            }
        }
    }

    /** Devuelve la disponibilidad de cupos por sección. */
    fun getAvailability(): Map<Char, Int> {
        return sectionsLetters.associateWith { letter ->
            val occupied = seatingState[letter]?.size ?: 0
            capacityPerSection - occupied
        }
    }

    // -------- HELPERS --------

    private fun formatPlace(section: Char, row: Int): String {
        val rowStr = row.toString().padStart(2, '0')
        return "$section-$rowStr"
    }

    private fun parsePlace(place: String): Pair<Char, Int>? {
        if (place.length !in 4..5) return null
        val section = place.first()
        val dashIndex = place.indexOf('-')
        if (dashIndex != 1) return null
        val rowStr = place.substring(dashIndex + 1)
        val row = rowStr.toIntOrNull() ?: return null
        if (section !in 'A'..'Z') return null
        if (row !in 1..capacityPerSection) return null
        return section to row
    }
}
