package com.example.planificadorasientos.domain.usecase

import com.example.planificadorasientos.domain.model.Student

class SeatAssignmentUseCase {

    private val sectionsLetters: List<Char> = ('A'..'Z').toList()
    private val capacityPerSection: Int = 12
    private val seatingState: MutableMap<Char, MutableSet<Int>> = mutableMapOf()

    fun assignRandomPlace(students: MutableList<Student>, studentId: String): String {
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
        val idx = students.indexOf(student)
        if (idx >= 0) students[idx] = student.copy(assigned = true, place = place)
        return place
    }

    fun unassignPlace(students: MutableList<Student>, studentId: String) {
        val student = students.firstOrNull { it.id == studentId } ?: return
        val place = student.place ?: return
        val parsed = parsePlace(place) ?: return
        val (section, row) = parsed
        seatingState[section]?.remove(row)
        val idx = students.indexOf(student)
        if (idx >= 0) students[idx] = student.copy(assigned = false, place = null)
    }

    fun resetSeating(students: MutableList<Student>) {
        seatingState.clear()
        for (i in students.indices) {
            val st = students[i]
            if (st.assigned || st.place != null) {
                students[i] = st.copy(assigned = false, place = null)
            }
        }
    }

    fun getAvailability(): Map<Char, Int> {
        return sectionsLetters.associateWith { letter ->
            val occupied = seatingState[letter]?.size ?: 0
            capacityPerSection - occupied
        }
    }

    private fun formatPlace(section: Char, row: Int): String =
        "$section-${row.toString().padStart(2, '0')}"

    private fun parsePlace(place: String): Pair<Char, Int>? {
        val section = place.firstOrNull() ?: return null
        val parts = place.split("-")
        if (parts.size != 2) return null
        val row = parts[1].toIntOrNull() ?: return null
        return section to row
    }
}
