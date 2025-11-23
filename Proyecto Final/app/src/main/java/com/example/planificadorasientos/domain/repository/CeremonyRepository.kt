package com.example.planificadorasientos.domain.repository

import com.example.planificadorasientos.domain.model.Ceremony

interface CeremonyRepository {

    suspend fun getCeremonies(): List<Ceremony>

    suspend fun addCeremony(ceremony: Ceremony)

    suspend fun updateCeremony(ceremony: Ceremony)

    suspend fun deleteCeremony(ceremonyId: String)
}
