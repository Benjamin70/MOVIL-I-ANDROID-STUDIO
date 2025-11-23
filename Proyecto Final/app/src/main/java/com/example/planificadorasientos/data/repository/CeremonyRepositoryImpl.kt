package com.example.planificadorasientos.data.repository

import com.example.planificadorasientos.data.local.LocalDataSource
import com.example.planificadorasientos.domain.model.Ceremony
import com.example.planificadorasientos.domain.repository.CeremonyRepository

/**
 * Implementación concreta del repositorio de ceremonias.
 * Combina los datos locales con Firebase.
 */
class CeremonyRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSource(),
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
) : CeremonyRepository {

    override suspend fun getCeremonies(): List<Ceremony> {
        val firebaseCeremonies = firebaseRepository.getCeremonies()
        localDataSource.ceremonies.clear()
        localDataSource.ceremonies.addAll(firebaseCeremonies)
        return localDataSource.ceremonies
    }

    override suspend fun addCeremony(ceremony: Ceremony) {
        firebaseRepository.addCeremony(ceremony)
        localDataSource.ceremonies.add(ceremony)
    }

    override suspend fun updateCeremony(ceremony: Ceremony) {
        firebaseRepository.updateCeremony(ceremony)
        localDataSource.updateCeremony(ceremony)
    }

    override suspend fun deleteCeremony(ceremonyId: String) {
        firebaseRepository.deleteCeremony(ceremonyId)
        localDataSource.removeCeremonyById(ceremonyId)
    }
}
