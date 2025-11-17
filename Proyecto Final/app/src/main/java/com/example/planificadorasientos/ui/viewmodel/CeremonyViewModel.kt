package com.example.planificadorasientos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planificadorasientos.data.model.Ceremony
import com.example.planificadorasientos.data.model.DataRepository
import com.example.planificadorasientos.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CeremonyViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _ceremonies = MutableStateFlow<List<Ceremony>>(emptyList())
    val ceremonies: StateFlow<List<Ceremony>> = _ceremonies

    // =======================
    // 🔹 CARGAR DATOS
    // =======================
    fun loadCeremonies() {
        viewModelScope.launch {
            try {
                val firebaseCeremonies = repository.getCeremonies()

                // 🔄 Sincronizar con DataRepository
                DataRepository.ceremonies.clear()
                DataRepository.ceremonies.addAll(firebaseCeremonies)

                _ceremonies.value = firebaseCeremonies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 AGREGAR CEREMONIA
    // =======================
    fun addCeremony(ceremony: Ceremony) {
        viewModelScope.launch {
            try {
                repository.addCeremony(ceremony)
                DataRepository.addCeremony(ceremony)
                loadCeremonies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 ACTUALIZAR CEREMONIA
    // =======================
    fun updateCeremony(ceremony: Ceremony) {
        viewModelScope.launch {
            try {
                repository.updateCeremony(ceremony)
                DataRepository.updateCeremony(ceremony)
                loadCeremonies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // =======================
    // 🔹 ELIMINAR CEREMONIA
    // =======================
    fun deleteCeremony(ceremonyId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCeremony(ceremonyId)
                DataRepository.removeCeremonyById(ceremonyId)
                loadCeremonies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
