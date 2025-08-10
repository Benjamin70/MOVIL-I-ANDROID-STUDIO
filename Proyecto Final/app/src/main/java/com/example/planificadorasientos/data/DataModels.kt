package com.example.planificadorasientos.data

import androidx.compose.runtime.mutableStateListOf
import kotlin.random.Random

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
    val assigned: Boolean = false,
    val place: String? = null
)

data class Ceremony(
    val id: String,
    val faculty: String,
    val date: String,
    val time: String
)

object DataRepository {

    // --------- Datos base (como ya tenías) ---------
    val students = mutableStateListOf(
        Student("2021001", "Luis Gómez", "Ingeniería", "Sistemas"),
        Student("2021002", "Ana Pérez", "Derecho", "Abogado"),
        Student("2021003", "Juan Torres", "Ingeniería", "Industrial"),
        Student("2021004", "María García", "Derecho", "Abogado Litigante"),
        Student("2021005", "Carlos Díaz", "Derecho", "Abogado Penal"),
        Student("2021006", "Pedro Peña", "Ingeniería", "Electrónica"),
        Student("2021007", "Lucía Reyes", "Derecho", "Abogado Legal")
    )

    val ceremonies = mutableStateListOf(
        Ceremony("1", "Ingeniería", "2025-07-20", "10:00 AM"),
        Ceremony("2", "Derecho", "2025-07-20", "02:00 PM")
    )

    val uniqueFaculties: List<String>
        get() = students.map { it.faculty }.distinct()

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

    // --------- Lógica de asignación A-01 … Z-12 ---------

    // Letras de secciones y capacidad fija por sección
    private val sectionsLetters: List<Char> = ('A'..'Z').toList()
    private const val capacityPerSection: Int = 12

    // Ocupación actual: por cada sección, filas ocupadas (1..12)
    private val seatingState: MutableMap<Char, MutableSet<Int>> = mutableMapOf()

    /**
     * Asigna aleatoriamente una sección (A..Z) con cupo y una fila libre (1..12),
     * actualiza el estudiante (assigned = true, place = "X-YY") y devuelve el place.
     * Si ya estaba asignado, devuelve su place actual. Si no hay cupos, devuelve "".
     */
    fun assignRandomPlace(studentId: String): String {
        val student = students.firstOrNull { it.id == studentId } ?: return ""
        if (student.assigned && student.place != null) return student.place

        // Secciones con cupo disponible
        val availableSections = sectionsLetters.filter { letter ->
            val occupied = seatingState[letter]?.size ?: 0
            occupied < capacityPerSection
        }
        if (availableSections.isEmpty()) return ""

        // Escoger sección y fila libres al azar
        val section = availableSections.random()
        val occupiedRows = seatingState.getOrPut(section) { mutableSetOf() }
        val freeRows = (1..capacityPerSection).filter { it !in occupiedRows }
        if (freeRows.isEmpty()) return "" // defensa extra
        val row = freeRows.random()

        // Marcar ocupación y actualizar estudiante
        occupiedRows.add(row)
        val place = formatPlace(section, row)
        updateStudent(student.copy(assigned = true, place = place))
        return place
    }

    /**
     * Desasigna el lugar del estudiante (si tenía), libera la fila en seatingState
     * y marca assigned = false, place = null.
     */
    fun unassignPlace(studentId: String) {
        val student = students.firstOrNull { it.id == studentId } ?: return
        val place = student.place ?: return

        val parsed = parsePlace(place) ?: return
        val (section, row) = parsed
        seatingState[section]?.remove(row)
        updateStudent(student.copy(assigned = false, place = null))
    }

    /**
     * Limpia toda la ocupación y deja a todos los estudiantes sin asignación.
     */
    fun resetSeating() {
        seatingState.clear()
        val snapshot = students.toList()
        snapshot.forEach { st ->
            if (st.assigned || st.place != null) {
                updateStudent(st.copy(assigned = false, place = null))
            }
        }
    }

    /**
     * Devuelve disponibilidad por sección: cupos libres = 12 - ocupados.
     */
    fun getAvailability(): Map<Char, Int> {
        return sectionsLetters.associateWith { letter ->
            val occupied = seatingState[letter]?.size ?: 0
            capacityPerSection - occupied
        }
    }

    // --------- Helpers ---------

    private fun formatPlace(section: Char, row: Int): String {
        val rowStr = row.toString().padStart(2, '0')
        return "$section-$rowStr"
    }

    private fun parsePlace(place: String): Pair<Char, Int>? {
        // Formato esperado: X-YY
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
