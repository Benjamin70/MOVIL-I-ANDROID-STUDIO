package com.example.planificadorasientos.domain.model

data class Student(
    val id: String = "",
    val name: String = "",
    val faculty: String = "",
    val career: String = "",
    val assigned: Boolean = false,
    val place: String? = null
)
