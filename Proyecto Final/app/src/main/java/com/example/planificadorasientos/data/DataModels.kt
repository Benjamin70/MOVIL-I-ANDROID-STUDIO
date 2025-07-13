package com.example.planificadorasientos.data

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
    val assigned: Boolean = false
)

data class Ceremony(
    val id: String,
    val faculty: String,
    val date: String,
    val time: String
)